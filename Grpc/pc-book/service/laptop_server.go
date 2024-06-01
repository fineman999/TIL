package service

import (
	"bytes"
	"context"
	"errors"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"io"
	"log"
	pb "pc-book/pb/proto"
	"pc-book/repository"
)

const maxImageSize = 1 << 30 // 1 GB

type LaptopServer struct {
	pb.LaptopServiceServer // LaptopServiceServer 인터페이스를 임베딩
	laptopStore            repository.LaptopStore
	imageStore             repository.ImageStore
	ratingStore            repository.RatingStore
}

func NewLaptopServer(laptopStore repository.LaptopStore, imageStore repository.ImageStore, ratingStore repository.RatingStore) *LaptopServer {
	return &LaptopServer{laptopStore: laptopStore, imageStore: imageStore, ratingStore: ratingStore}
}

func (server *LaptopServer) RateLaptop(stream pb.LaptopService_RateLaptopServer) error {
	for {
		err := server.checkContextError(stream.Context())
		if err != nil {
			return err
		}

		req, err := stream.Recv()
		if err != nil {
			if errors.Is(err, io.EOF) {
				return nil
			}
			return logError(status.Errorf(codes.Unknown, "cannot receive rating request: %v", err))
		}

		laptopID := req.GetLaptopId()
		score := req.GetScore()

		log.Printf("Receive a rate-laptop request for laptop %s with score %.2f", laptopID, score)

		found, err := server.laptopStore.Find(laptopID)
		if err != nil {
			return logError(status.Errorf(codes.Internal, "cannot find laptop: %v", err))
		}

		if found == nil {
			return logError(status.Errorf(codes.NotFound, "laptop %s not found", laptopID))
		}

		rating, err := server.ratingStore.Add(laptopID, score)
		if err != nil {
			return logError(status.Errorf(codes.Internal, "cannot add rating to the store: %v", err))
		}

		res := &pb.RateLaptopResponse{
			LaptopId:     laptopID,
			Count:        rating.Count,
			AverageScore: rating.Sum / float64(rating.Count),
		}

		err = stream.Send(res)
		if err != nil {
			return logError(status.Errorf(codes.Internal, "cannot send rating response: %v", err))
		}
	}
	return nil
}

func (server *LaptopServer) UploadImage(stream pb.LaptopService_UploadImageServer) error {
	req, err := stream.Recv()
	if err != nil {
		return logError(status.Errorf(codes.Unknown, "cannot receive image info: %v", err))
	}

	laptopID := req.GetInfo().GetLaptopId()
	imageType := req.GetInfo().GetImageType()
	log.Printf("Receive an upload-image request for laptop %s with image type %s", laptopID, imageType)

	laptop, err := server.laptopStore.Find(laptopID)
	if err != nil {
		return logError(status.Errorf(codes.Internal, "cannot find laptop: %v", err))
	}

	if laptop == nil {
		return logError(status.Errorf(codes.InvalidArgument, "laptop %s doesn't exist", laptopID))
	}

	imageData := bytes.Buffer{}
	imageSize := 0

	for {
		if err := server.checkContextError(stream.Context()); err != nil {
			return err
		}
		log.Print("Waiting to receive more data")

		req, err := stream.Recv()
		if err != nil {
			if errors.Is(err, io.EOF) {
				log.Print("No more data")
				break
			}
			if errors.Is(err, context.Canceled) {
				log.Print("Client has cancelled the upload")
				return logError(status.Error(codes.Canceled, "Client has cancelled the upload"))
			}
			return logError(status.Errorf(codes.Unknown, "cannot receive chunk data: %v", err))
		}

		chunk := req.GetChunkData()
		size := len(chunk)

		imageSize += size
		if imageSize > maxImageSize {
			return logError(status.Errorf(codes.InvalidArgument, "image is too large: %d > %d", imageSize, maxImageSize))
		}

		// make heavy
		//time.Sleep(time.Second)
		// chunk의 내용을 imageData 버퍼의 끝에 추가
		n, err := imageData.Write(chunk)
		if err != nil {
			return logError(status.Errorf(codes.Internal, "cannot write chunk data: %v", err))
		}
		log.Printf("Write %d bytes to image data buffer", n)
		log.Printf("Total size of image data buffer: %.2f KB", float64(imageData.Len())/1024)
	}

	imageID, err := server.imageStore.Save(laptopID, imageType, imageData)
	if err != nil {
		return logError(status.Errorf(codes.Internal, "cannot save image to the store: %v", err))
	}

	res := &pb.UploadImageResponse{
		Id:   imageID,
		Size: uint32(imageSize),
	}

	err = stream.SendAndClose(res)
	if err != nil {
		return logError(status.Errorf(codes.Unknown, "cannot send response: %v", err))
	}

	log.Printf("Image with id %s and size %d has been saved", imageID, imageSize)
	return nil

}

func (server *LaptopServer) SearchLaptop(req *pb.SearchLaptopRequest, stream pb.LaptopService_SearchLaptopServer) error {
	filter := req.GetFilter()
	log.Printf("Receive a search-laptop request with filter: %v", filter)

	err := server.laptopStore.Search(
		stream.Context(),
		filter, func(laptop *pb.Laptop) error {
			res := &pb.SearchLaptopResponse{Laptop: laptop}
			err := stream.Send(res)
			if err != nil {
				return err
			}

			log.Printf("Send laptop with id: %s", laptop.GetId())
			return nil
		})

	if err != nil {
		return status.Errorf(codes.Internal, "Unexpected error: %v", err)
	}

	return nil
}

func (server *LaptopServer) CreateLaptop(ctx context.Context, req *pb.CreateLaptopRequest) (*pb.CreateLaptopResponse, error) {
	laptop := req.GetLaptop()
	log.Printf("Receive a create-laptop request with id: %s", laptop.GetId())

	if len(laptop.GetId()) > 0 {
		_, err := uuid.Parse(laptop.GetId())
		if err != nil {
			return nil, status.Errorf(codes.InvalidArgument, "Laptop ID is not a valid UUID: %v", err)
		}
	} else {
		id, err := uuid.NewRandom()
		if err != nil {
			return nil, status.Errorf(codes.Internal, "Cannot generate a new laptop ID: %v", err)
		}
		laptop.Id = id.String()
	}

	// some heavy processing
	//time.Sleep(6 * time.Second)

	ctxErr := server.checkContextError(ctx)
	if ctxErr != nil {
		return nil, ctxErr
	}

	err := server.laptopStore.Save(laptop)
	if err != nil {
		code := codes.Internal
		if errors.Is(err, repository.ErrAlreadyExists) {
			code = codes.AlreadyExists
		}
		return nil, status.Errorf(code, "Cannot save laptop to the store: %v", err)
	}

	log.Printf("Laptop with id %s is saved to the store", laptop.GetId())

	res := &pb.CreateLaptopResponse{
		Id: laptop.GetId(),
	}
	return res, nil
}

func (server *LaptopServer) checkContextError(ctx context.Context) error {
	switch err := ctx.Err(); {
	case errors.Is(err, context.Canceled):
		return logError(status.Error(codes.Canceled, "The client has cancelled the request"))
	case errors.Is(err, context.DeadlineExceeded):
		return logError(status.Error(codes.DeadlineExceeded, "The deadline has exceeded"))
	default:
		return nil
	}
}

func logError(err error) error {
	if err != nil {
		log.Print(err)
	}
	return nil
}
