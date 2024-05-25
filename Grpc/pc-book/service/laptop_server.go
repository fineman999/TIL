package service

import (
	"context"
	"errors"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"log"
	pb "pc-book/pb/proto"
	"time"
)

type LaptopServer struct {
	pb.LaptopServiceServer // LaptopServiceServer 인터페이스를 임베딩
	Store                  LaptopStore
}

func NewLaptopServer(store LaptopStore) *LaptopServer {
	return &LaptopServer{Store: store}
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
	time.Sleep(6 * time.Second)

	ctxErr := server.checkContextError(ctx)
	if ctxErr != nil {
		return nil, ctxErr
	}

	err := server.Store.Save(laptop)
	if err != nil {
		code := codes.Internal
		if errors.Is(err, ErrAlreadyExists) {
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
	if errors.Is(ctx.Err(), context.Canceled) {
		log.Print("The client has cancelled the request")
		return status.Error(codes.Canceled, "The client has cancelled the request")
	} else if errors.Is(ctx.Err(), context.DeadlineExceeded) {
		log.Print("The deadline has exceeded")
		return status.Error(codes.DeadlineExceeded, "The deadline has exceeded")
	}
	return nil
}
