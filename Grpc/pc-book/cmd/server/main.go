package main

import (
	"context"
	"crypto/tls"
	"crypto/x509"
	"flag"
	"fmt"
	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/reflection"
	"log"
	"net"
	"net/http"
	"os"
	"pc-book/domain"
	"pc-book/interceptor"
	pb "pc-book/pb/proto"
	"pc-book/repository"
	"pc-book/service"
	"pc-book/util"
	"time"
)

const (
	clientCACertFile = "cert/ca-cert.pem"
	serverCertFile   = "cert/server-cert.pem"
	serverKeyFile    = "cert/server-key.pem"
)

func loadTLSCredentials() (credentials.TransportCredentials, error) {
	// Load certificate of the CA who signed client's certificate
	pemClientCA, err := os.ReadFile(clientCACertFile)
	if err != nil {
		return nil, err
	}

	certPool := x509.NewCertPool()
	if !certPool.AppendCertsFromPEM(pemClientCA) {
		return nil, err
	}
	// Load server's certificate and private key
	serverCert, err := tls.LoadX509KeyPair(serverCertFile, serverKeyFile)
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

func runGRPCServer(authServer pb.AuthServiceServer, laptopServer pb.LaptopServiceServer, jwtManager *util.JWTManager, enableTLS bool, listener net.Listener) error {
	authInterceptor := interceptor.NewAuthInterceptor(jwtManager, accessibleRoles())
	serverOptions := []grpc.ServerOption{
		grpc.UnaryInterceptor(authInterceptor.Unary()),
		grpc.StreamInterceptor(authInterceptor.Stream()),
	}
	if enableTLS {
		tlsCredentials, err := loadTLSCredentials()
		if err != nil {
			return fmt.Errorf("cannot load TLS credentials: %v", err)
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

	log.Printf("gRPC server is running on port: %d", listener.Addr().(*net.TCPAddr).Port)
	log.Printf("Enable TLS: %t", enableTLS)
	return grpcServer.Serve(listener)
}

func runRESTServer(
	authServer pb.AuthServiceServer,
	laptopServer pb.LaptopServiceServer,
	jwtManager *util.JWTManager,
	enableTLS bool,
	listener net.Listener,
	grpcEndpoint string,
) error {
	mux := runtime.NewServeMux()
	dialOptions := []grpc.DialOption{grpc.WithTransportCredentials(insecure.NewCredentials())}
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	// err := pb.RegisterAuthServiceHandlerServer(ctx, mux, authServer)
	// streaming을 지원하지 않는 REST API를 gRPC로 변환할 때 사용 - Unary로 변환
	err := pb.RegisterAuthServiceHandlerFromEndpoint(ctx, mux, grpcEndpoint, dialOptions)
	if err != nil {
		return err
	}

	//err = pb.RegisterLaptopServiceHandlerServer(ctx, mux, laptopServer)
	// streaming을 지원하지 않는 REST API를 gRPC로 변환할 때 사용 - Unary로 변환
	err = pb.RegisterLaptopServiceHandlerFromEndpoint(ctx, mux, grpcEndpoint, dialOptions)
	if err != nil {
		return err
	}

	log.Printf("REST server is running on port: %d", listener.Addr().(*net.TCPAddr).Port)
	log.Printf("Enable TLS: %t", enableTLS)
	if enableTLS {
		return http.ServeTLS(listener, mux, serverCertFile, serverKeyFile)
	}
	return http.Serve(listener, mux)
}

func main() {
	// "port"라는 이름의 명령줄 인자를 정의합니다. 기본값은 8080이며, 설명은 "server port"입니다.
	// go run main.go -port=8081
	port := flag.Int("port", 8080, "server port")
	enableTLS := flag.Bool("tls", false, "enable SSL/TLS")
	serverType := flag.String("type", "grpc", "type of server (grpc/rest)")
	endPoint := flag.String("endpoint", "", "gRPC server endpoint")
	flag.Parse()
	log.Printf("Server started on port %d, TLS: %t", *port, *enableTLS)

	laptopStore := repository.NewInMemoryLaptopStore()
	imageStore := repository.NewDiskImageStore("img")
	ratingStore := repository.NewInMemoryRatingStore()
	userStore := repository.NewInMemoryUserStore()
	err2 := seedUsers(userStore)
	if err2 != nil {
		log.Fatalf("cannot seed users: %v", err2)
	}
	jwtManager := util.NewJWTManager(secretKey, tokenDuration)

	authServer := service.NewAuthServer(userStore, jwtManager)
	laptopServer := service.NewLaptopServer(laptopStore, imageStore, ratingStore)
	address := fmt.Sprintf("0.0.0.0:%d", *port)
	listener, err := net.Listen("tcp", address)
	if err != nil {
		log.Fatalf("cannot start server: %v", err)
	}
	if *serverType == "grpc" {
		err = runGRPCServer(authServer, laptopServer, jwtManager, *enableTLS, listener)
	} else {
		err = runRESTServer(authServer, laptopServer, jwtManager, *enableTLS, listener, *endPoint)
	}
	if err != nil {
		log.Fatalf("cannot run grpc server: %v", err)
	}
}
