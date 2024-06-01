package client

import (
	"bufio"
	"context"
	"errors"
	"fmt"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"io"
	"log"
	"os"
	"path/filepath"
	pb "pc-book/pb/proto"
	"pc-book/sample"
	"strings"
	"time"
)

type LaptopClient struct {
	service pb.LaptopServiceClient
}

func NewLaptopClient(cc *grpc.ClientConn) *LaptopClient {
	service := pb.NewLaptopServiceClient(cc)
	return &LaptopClient{service: service}
}

func (laptopClient *LaptopClient) TestRateLaptop() {
	n := 3
	laptopIDs := make([]string, n)

	for i := 0; i < n; i++ {
		laptop := sample.NewLaptop()
		laptopClient.createLaptop(laptop)
		laptopIDs[i] = laptop.GetId()
	}

	scores := make([]float64, n)
	for {
		fmt.Print("rate laptop (y/n): ")
		var answer string
		fmt.Scan(&answer)

		if strings.ToLower(answer) != "y" {
			break
		}

		for i := 0; i < n; i++ {
			scores[i] = sample.RandomLaptopScore()
		}

		err := laptopClient.rateLaptop(laptopIDs, scores)
		if err != nil {
			log.Fatalf("cannot rate laptop: %v", err)
		}
	}
}

func (laptopClient *LaptopClient) testUploadImage() {
	laptop := sample.NewLaptop()
	laptopClient.createLaptop(laptop)
	laptopClient.uploadFile(laptop.GetId(), "tmp/laptop.jpg")
}

func (laptopClient *LaptopClient) uploadFile(laptopId string, imagePath string) {
	file, err := os.Open(imagePath)
	if err != nil {
		log.Fatalf("cannot open image file: %v", err)
	}
	defer file.Close()

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	stream, err := laptopClient.service.UploadImage(ctx)
	if err != nil {
		err2 := stream.RecvMsg(nil)
		log.Fatalf("cannot upload image: %v, %v", err, err2)
	}

	req := &pb.UploadImageRequest{
		Data: &pb.UploadImageRequest_Info{
			Info: &pb.ImageInfo{
				LaptopId:  laptopId,
				ImageType: filepath.Ext(imagePath),
			},
		},
	}

	err = stream.Send(req)
	if err != nil {
		err2 := stream.RecvMsg(nil)
		log.Fatalf("cannot send image info: %v, %v", err, err2)
	}

	reader := bufio.NewReader(file)
	buffer := make([]byte, 1024)

	for {
		// buffer: 읽은 데이터를 저장할 공간
		// n: 실제로 읽은 데이터의 크기
		n, err := reader.Read(buffer)
		if err != nil {

			if errors.Is(err, io.EOF) {
				break
			}
			log.Fatalf("cannot read chunk to buffer: %v", err)

		}

		req := &pb.UploadImageRequest{
			Data: &pb.UploadImageRequest_ChunkData{
				// buffer[:n]: buffer의 0번째부터 n-1번째까지의 데이터
				ChunkData: buffer[:n],
			},
		}

		err = stream.Send(req)
		if err != nil {
			err2 := stream.RecvMsg(nil)
			log.Fatalf("cannot send chunk to server: %v, %v", err, err2)
		}
	}

	res, err := stream.CloseAndRecv()
	if err != nil {
		err2 := stream.RecvMsg(nil)
		log.Fatalf("cannot receive response: %v, %v", err, err2)
	}

	log.Printf("image uploaded with id: %s, size: %d", res.GetId(), res.GetSize())
}

func (laptopClient *LaptopClient) testCreateAndSearchLaptop() {
	for i := 0; i < 10; i++ {
		laptopClient.createLaptop(sample.NewLaptop())
	}
	filter := &pb.Filter{
		MaxPriceUsd: 2000,
		MinCpuCores: 4,
		MinCpuGhz:   2.5,
		MinRam:      &pb.Memory{Value: 8, Unit: pb.Memory_GIGABYTE},
	}
	laptopClient.searchLaptop(filter)
}

func (laptopClient *LaptopClient) searchLaptop(filter *pb.Filter) {
	log.Print("searching laptop with filter: ", filter)
	req := &pb.SearchLaptopRequest{Filter: filter}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	stream, err := laptopClient.service.SearchLaptop(ctx, req)
	if err != nil {
		log.Fatalf("cannot search laptop: %v", err)
	}

	for {
		res, err := stream.Recv()
		if err == io.EOF {
			log.Print("no more laptop")
			break
		}
		if err != nil {
			if status.Code(err) == codes.Canceled {
				log.Print("search laptop canceled")
				return
			}
			log.Fatalf("cannot receive response: %v", err)
		}

		laptop := res.GetLaptop()
		log.Printf("- found laptop with id: %s", laptop.GetId())
		log.Printf("  + brand: %s", laptop.GetBrand())
		log.Printf("  + name: %s", laptop.GetName())
		log.Printf("  + cpu: %s - %v cores - %.2f GHz", laptop.GetCpu().GetBrand(), laptop.GetCpu().GetNumberCores(), laptop.GetCpu().GetMinGhz())
		log.Printf("  + ram: %v %s", laptop.GetMemory().GetValue(), laptop.GetMemory().GetUnit())
		log.Printf("  + price: %.2f USD", laptop.GetPriceUsd())
	}
}

func (laptopClient *LaptopClient) createLaptop(laptop *pb.Laptop) {
	//laptop.Id = "invalid-uuid"
	req := &pb.CreateLaptopRequest{Laptop: laptop}

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	res, err := laptopClient.service.CreateLaptop(ctx, req)
	if err != nil {
		st, ok := status.FromError(err)
		if ok && st.Code() == codes.AlreadyExists {
			log.Print("laptop already exists")
		} else {
			log.Fatalf("cannot create laptop: %v", err)
		}
		return
	}

	log.Printf("create laptop with id: %s", res.Id)
}

func (laptopClient *LaptopClient) rateLaptop(laptopIDs []string, scores []float64) error {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	stream, err := laptopClient.service.RateLaptop(ctx)
	if err != nil {
		return err
	}

	waitResponse := make(chan error)
	go func() {
		for {
			res, err := stream.Recv()
			if err == io.EOF {
				log.Print("no more response")
				waitResponse <- nil
				return
			}
			if err != nil {
				log.Printf("cannot receive response: %v", err)
				waitResponse <- err
				return
			}

			log.Printf("received response: %v", res)
		}
	}()

	for i, laptopID := range laptopIDs {
		req := &pb.RateLaptopRequest{
			LaptopId: laptopID,
			Score:    scores[i],
		}

		err := stream.Send(req)
		if err != nil {
			return err
		}
	}

	err = stream.CloseSend()
	if err != nil {
		return err
	}

	return <-waitResponse
}
