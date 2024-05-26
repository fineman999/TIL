package main

import (
	"context"
	"flag"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/status"
	"io"
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
	for i := 0; i < 10; i++ {
		createLaptop(client)
	}
	filter := &pb.Filter{
		MaxPriceUsd: 2000,
		MinCpuCores: 4,
		MinCpuGhz:   2.5,
		MinRam:      &pb.Memory{Value: 8, Unit: pb.Memory_GIGABYTE},
	}
	searchLaptop(client, filter)
}

func searchLaptop(client pb.LaptopServiceClient, filter *pb.Filter) {
	log.Print("searching laptop with filter: ", filter)
	req := &pb.SearchLaptopRequest{Filter: filter}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	stream, err := client.SearchLaptop(ctx, req)
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

func createLaptop(client pb.LaptopServiceClient) {
	laptop := sample.NewLaptop()
	//laptop.Id = "invalid-uuid"
	req := &pb.CreateLaptopRequest{Laptop: laptop}

	ctx, cancel := context.WithCancel(context.Background())
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
