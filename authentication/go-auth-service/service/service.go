package service

import (
	"context"
	"fmt"
	"go-auth-service/auth"
	"go-auth-service/config"
	"go-auth-service/repository"
)

type Service struct {
	cfg        *config.Config
	repository *repository.Repository
	oauthConf  *auth.OAuth
}

func NewService(
	cfg *config.Config,
	repository *repository.Repository,
	oauth2 *auth.OAuth,
) (*Service, error) {
	s := &Service{cfg: cfg,
		repository: repository,
		oauthConf:  oauth2,
	}
	return s, nil
}

func (s *Service) TwitterOAuth(ctx context.Context, code, state string) {
	authenticate, err := s.oauthConf.TwitterAuthenticate(ctx, code, state)
	if err != nil {
		return
	}
	fmt.Println(authenticate)
}

func (s *Service) GoogleOAuth(ctx context.Context, code string) {

	authenticate, err := s.oauthConf.GoogleAuthenticate(ctx, code)
	if err != nil {
		return
	}
	fmt.Println(authenticate)
}

func (s *Service) GetPkceInfo() *auth.Pkce {
	return s.oauthConf.GetPKCE()
}
