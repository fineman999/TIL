package service

import (
	"rpc-server/config"
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
