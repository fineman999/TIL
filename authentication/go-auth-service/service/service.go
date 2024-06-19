package service

import (
	"go-auth-service/auth"
	"go-auth-service/config"
	"go-auth-service/repository"
)

type Service struct {
	cfg          *config.Config
	repository   *repository.Repository
	twitterOAuth *auth.TwitterAuth
}

func NewService(
	cfg *config.Config,
	repository *repository.Repository,
	oauth2 *auth.TwitterAuth,
) (*Service, error) {
	s := &Service{cfg: cfg,
		repository:   repository,
		twitterOAuth: oauth2,
	}
	return s, nil
}

func (s *Service) TwitterOAuth() {

}
