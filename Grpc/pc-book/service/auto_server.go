package service

import (
	"context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	pb "pc-book/pb/proto"
	"pc-book/repository"
	"pc-book/util"
)

type AuthServer struct {
	pb.AuthServiceServer
	userStore  repository.UserStore
	jwtManager *util.JWTManager
}

func (server *AuthServer) Login(ctx context.Context, req *pb.LoginRequest) (*pb.LoginResponse, error) {
	user, err := server.userStore.Find(req.GetUsername())
	if err != nil {
		return nil, status.Errorf(codes.NotFound, "user not found: %v", err)
	}

	if user == nil || !user.IsCorrectPassword(req.GetPassword()) {
		return nil, status.Error(codes.Unauthenticated, "invalid credentials")
	}

	token, err := server.jwtManager.Generate(user)
	if err != nil {
		return nil, status.Error(codes.Internal, "cannot generate access token")
	}

	return &pb.LoginResponse{Token: token}, nil
}

func NewAuthServer(userStore repository.UserStore, jwtManager *util.JWTManager) *AuthServer {
	return &AuthServer{userStore: userStore, jwtManager: jwtManager}
}
