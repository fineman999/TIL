package main

import (
	"crypto/tls"
	"crypto/x509"
	"flag"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
	"google.golang.org/grpc/credentials/insecure"
	"log"
	"os"
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

func loadTLSCredentials() (credentials.TransportCredentials, error) {
	pemServerCA, err := os.ReadFile("cert/ca-cert.pem")
	if err != nil {
		return nil, err
	}

	certPool := x509.NewCertPool()
	if !certPool.AppendCertsFromPEM(pemServerCA) {
		return nil, err
	}

	// Load client's certificate and private key
	clientCert, err := tls.LoadX509KeyPair("cert/client-cert.pem", "cert/client-key.pem")
	if err != nil {
		return nil, err
	}

	config := &tls.Config{
		Certificates: []tls.Certificate{clientCert},
		RootCAs:      certPool,
	}

	return credentials.NewTLS(config), nil
}

func main() {
	serverAddress := flag.String("address", "0.0.0.0:8080", "server address")
	enableTLS := flag.Bool("tls", false, "enable SSL/TLS")
	flag.Parse()
	log.Printf("server address: %s, enable TLS: %t", *serverAddress, *enableTLS)

	transportOptions := grpc.WithTransportCredentials(insecure.NewCredentials())
	if *enableTLS {
		tlsCredentials, err := loadTLSCredentials()
		if err != nil {
			log.Fatalf("cannot load TLS credentials: %v", err)
			return
		}
		transportOptions = grpc.WithTransportCredentials(tlsCredentials)
	}

	conn, err := grpc.NewClient(*serverAddress, transportOptions)
	if err != nil {
		log.Fatalf("cannot dial server: %v", err)
		return
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
		transportOptions,
		grpc.WithUnaryInterceptor(interceptor.Unary()),
		grpc.WithStreamInterceptor(interceptor.Stream()),
	)

	log.Printf("test rate laptop")
	laptopClient := client.NewLaptopClient(conn2)
	laptopClient.TestRateLaptop()
}
