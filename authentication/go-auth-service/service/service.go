package service

import (
	"context"
	"fmt"
	"github.com/dghubble/go-twitter/twitter"
	"go-auth-service/auth"
	"go-auth-service/config"
	"go-auth-service/repository"
)

type Service struct {
	cfg               *config.Config
	repository        *repository.Repository
	oauthConf         *auth.OAuth
	oauth1TwitterConf *auth.TwitterOAuth1
}

func NewService(
	cfg *config.Config,
	repository *repository.Repository,
	oauth2 *auth.OAuth,
	oauth1 *auth.TwitterOAuth1,
) (*Service, error) {
	s := &Service{cfg: cfg,
		repository:        repository,
		oauthConf:         oauth2,
		oauth1TwitterConf: oauth1,
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

func (s *Service) StartOAuth1(_ context.Context) (*auth.OAuth1Token, error) {
	info, err := s.oauth1TwitterConf.GetOAuthInfo()
	if err != nil {
		return nil, fmt.Errorf("failed to get oauth info: %w", err)
	}
	return info, nil
}

func (s *Service) CallbackOAuth1(ctx context.Context, oauthToken, oauthVerifier string) (*twitter.User, error) {

	verifyOAuth, err := s.oauth1TwitterConf.VerifyOAuth(oauthToken, oauthVerifier)
	if err != nil {
		return nil, fmt.Errorf("failed to verify oauth: %w", err)
	}
	userInformation, err := s.oauth1TwitterConf.GetUserInformation(ctx, verifyOAuth)
	if err != nil {
		return nil, fmt.Errorf("failed to get user information: %w", err)
	}
	return userInformation, nil
}
