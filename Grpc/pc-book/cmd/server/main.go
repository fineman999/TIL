package main

import (
	"flag"
	"fmt"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
	"log"
	"net"
	pb "pc-book/pb/proto"
	"pc-book/service"
)

func main() {
	// "port"라는 이름의 명령줄 인자를 정의합니다. 기본값은 8080이며, 설명은 "server port"입니다.
	// go run main.go -port=8081
	port := flag.Int("port", 8080, "server port")
	flag.Parse()
	log.Printf("Server started on port %d", *port)

	laptopStore := service.NewInMemoryLaptopStore()
	imageStore := service.NewDiskImageStore("img")
	ratingStore := service.NewInMemoryRatingStore()
	laptopServer := service.NewLaptopServer(laptopStore, imageStore, ratingStore)
	grpcServer := grpc.NewServer()
	pb.RegisterLaptopServiceServer(grpcServer, laptopServer)
	/**
	서버에서 제공하는 RPC 메서드와,
	메시지의 정보를 동적으로 조회가능하게 해주며,
	이를 통해서 클라이언트가 서버에서 제공하는 메서드와 정보를 몰라도 탐색하고 검색할 수 있게 해줍니다.
	*/
	reflection.Register(grpcServer)

	address := fmt.Sprintf("0.0.0.0:%d", *port)
	listener, err := net.Listen("tcp", address)
	if err != nil {
		log.Fatalf("cannot start server: %v", err)
	}

	if err := grpcServer.Serve(listener); err != nil {
		log.Fatalf("cannot start server: %v", err)
	}
}
