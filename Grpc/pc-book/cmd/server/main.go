package main

import (
	"flag"
	"fmt"
	"google.golang.org/grpc"
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

	laptopServer := service.NewLaptopServer(service.NewInMemoryLaptopStore())
	grpcServer := grpc.NewServer()
	pb.RegisterLaptopServiceServer(grpcServer, laptopServer)

	address := fmt.Sprintf("0.0.0.0:%d", *port)
	listener, err := net.Listen("tcp", address)
	if err != nil {
		log.Fatalf("cannot start server: %v", err)
	}

	if err := grpcServer.Serve(listener); err != nil {
		log.Fatalf("cannot start server: %v", err)
	}
}
