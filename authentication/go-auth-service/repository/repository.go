package repository

import "go-auth-service/config"

type Repository struct {
	cfg *config.Config
}

func NewRepository(cfg *config.Config) (*Repository, error) {
	return &Repository{
		cfg: cfg,
	}, nil
}
