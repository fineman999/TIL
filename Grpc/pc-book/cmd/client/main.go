package main

import (
	"context"
	"flag"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/status"
	"log"
	pb "pc-book/pb/proto"
	"pc-book/sample"
	"time"
)

func main() {
	serverAddress := flag.String("address", "0.0.0.0:8080", "server address")
	flag.Parse()
	log.Printf("server address: %s", *serverAddress)

	conn, err := grpc.NewClient(*serverAddress, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("cannot dial server: %v", err)
	}

	// pb.NewLaptopServiceClient(conn): gRPC 클라이언트 생성
	client := pb.NewLaptopServiceClient(conn)
	laptop := sample.NewLaptop()
	//laptop.Id = "invalid-uuid"
	req := &pb.CreateLaptopRequest{Laptop: laptop}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	res, err := client.CreateLaptop(ctx, req)
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
