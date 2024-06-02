package main

import (
	"crypto/tls"
	"crypto/x509"
	"flag"
	"fmt"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
	"google.golang.org/grpc/reflection"
	"log"
	"net"
	"os"
	"pc-book/domain"
	"pc-book/interceptor"
	pb "pc-book/pb/proto"
	"pc-book/repository"
	"pc-book/service"
	"pc-book/util"
	"time"
)

func loadTLSCredentials() (credentials.TransportCredentials, error) {
	// Load certificate of the CA who signed client's certificate
	pemClientCA, err := os.ReadFile("cert/ca-cert.pem")
	if err != nil {
		return nil, err
	}

	certPool := x509.NewCertPool()
	if !certPool.AppendCertsFromPEM(pemClientCA) {
		return nil, err
	}
	// Load server's certificate and private key
	serverCert, err := tls.LoadX509KeyPair("cert/server-cert.pem", "cert/server-key.pem")
	if err != nil {
		return nil, err
	}

	// Create credentials
	config := &tls.Config{
		Certificates: []tls.Certificate{serverCert},
		//ClientAuth:   tls.NoClientCert, // 클라이언트 인증을 요구하지 않음(서버 사이드 TLS)
		ClientAuth: tls.RequireAndVerifyClientCert, // 클라이언트 인증을 요구하고 검증함(Mutual TLS)
		ClientCAs:  certPool,                       // 클라이언트 인증서를 검증하기 위한 CA 인증서
	}
	return credentials.NewTLS(config), nil
}

const (
	secretKey     = "secret"
	tokenDuration = 15 * time.Minute
)

func seedUsers(userStore repository.UserStore) error {
	err := createUser(userStore, "admin", "admin", "admin")
	if err != nil {
		log.Fatalf("cannot create user: %v", err)
		return err
	}
	return createUser(userStore, "user1", "user1", "user")
}
func createUser(userStore repository.UserStore, username, password, role string) error {
	user, err := domain.NewUser(username, password, role)
	if err != nil {
		return err
	}
	return userStore.Save(user)
}

func accessibleRoles() map[string][]string {
	const laptopServicePath = "/LaptopService/"
	return map[string][]string{
		laptopServicePath + "CreateLaptop": {"admin"},
		laptopServicePath + "UploadImage":  {"admin"},
		laptopServicePath + "RateLaptop":   {"user", "admin"},
	}
}

func main() {
	// "port"라는 이름의 명령줄 인자를 정의합니다. 기본값은 8080이며, 설명은 "server port"입니다.
	// go run main.go -port=8081
	port := flag.Int("port", 8080, "server port")
	enableTLS := flag.Bool("tls", false, "enable SSL/TLS")
	flag.Parse()
	log.Printf("Server started on port %d, TLS: %t", *port, *enableTLS)

	laptopStore := repository.NewInMemoryLaptopStore()
	imageStore := repository.NewDiskImageStore("img")
	ratingStore := repository.NewInMemoryRatingStore()
	userStore := repository.NewInMemoryUserStore()
	err2 := seedUsers(userStore)
	if err2 != nil {
		log.Fatalf("cannot seed users: %v", err2)
		return
	}
	jwtManager := util.NewJWTManager(secretKey, tokenDuration)

	authServer := service.NewAuthServer(userStore, jwtManager)
	laptopServer := service.NewLaptopServer(laptopStore, imageStore, ratingStore)
	authInterceptor := interceptor.NewAuthInterceptor(jwtManager, accessibleRoles())
	serverOptions := []grpc.ServerOption{
		grpc.UnaryInterceptor(authInterceptor.Unary()),
		grpc.StreamInterceptor(authInterceptor.Stream()),
	}
	if *enableTLS {
		tlsCredentials, err := loadTLSCredentials()
		if err != nil {
			log.Fatalf("cannot load TLS credentials: %v", err)
		}
		serverOptions = append(serverOptions, grpc.Creds(tlsCredentials))
	}
	grpcServer := grpc.NewServer(
		serverOptions...,
	)
	pb.RegisterAuthServiceServer(grpcServer, authServer)
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
