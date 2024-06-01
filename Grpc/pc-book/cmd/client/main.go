package main

import (
	"flag"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"log"
	client "pc-book/client"
	"time"
)

const (
	username        = "admin"
	password        = "admin"
	refreshDuration = 30 * time.Minute
)

func accessibleRoles() map[string]bool {
	const laptopServicePath = "/LaptopService/"
	return map[string]bool{
		laptopServicePath + "CreateLaptop": true,
		laptopServicePath + "UploadImage":  true,
		laptopServicePath + "RateLaptop":   true,
	}
}

func main() {
	serverAddress := flag.String("address", "0.0.0.0:8080", "server address")
	flag.Parse()
	log.Printf("server address: %s", *serverAddress)

	conn, err := grpc.NewClient(*serverAddress, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("cannot dial server: %v", err)
	}
	defer conn.Close()

	authClient := client.NewAuthClient(conn, username, password)
	interceptor, err := client.NewAuthInterceptor(authClient, accessibleRoles(), refreshDuration)
	if err != nil {
		log.Fatalf("cannot create auth interceptor: %v", err)
		return
	}

	conn2, err := grpc.NewClient(
		*serverAddress,
		grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithUnaryInterceptor(interceptor.Unary()),
		grpc.WithStreamInterceptor(interceptor.Stream()),
	)

	laptopClient := client.NewLaptopClient(conn2)
	laptopClient.TestRateLaptop()
}
