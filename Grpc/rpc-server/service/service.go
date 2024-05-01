package service

import (
	"rpc-server/config"
	auth "rpc-server/gRPC/proto"
	"rpc-server/repository"
)

type Service struct {
	cfg        *config.Config
	repository *repository.Repository
}

func NewService(cfg *config.Config, repository *repository.Repository) (*Service, error) {
	s := &Service{cfg: cfg, repository: repository}
	return s, nil
}

func (s *Service) CreateAuth(name string) (*auth.AuthData, error) {
	return s.repository.CreateAuth(name)
}
