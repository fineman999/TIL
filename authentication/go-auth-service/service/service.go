package service

import (
	"context"
	"fmt"
	"github.com/dghubble/go-twitter/twitter"
	"go-auth-service/config"
	"go-auth-service/oauth"
	"go-auth-service/repository"
	"go-auth-service/types"
)

type Service struct {
	cfg               *config.Config
	repository        *repository.Repository
	oauthClient       *oauth.OAuth
	oauth1TwitterConf *oauth.TwitterOAuth1
	jwtConfig         *oauth.JwtConfig
}

func NewService(
	cfg *config.Config,
	repository *repository.Repository,
	oauth2 *oauth.OAuth,
	oauth1 *oauth.TwitterOAuth1,
	jwtConfig *oauth.JwtConfig,
) (*Service, error) {
	s := &Service{cfg: cfg,
		repository:        repository,
		oauthClient:       oauth2,
		oauth1TwitterConf: oauth1,
		jwtConfig:         jwtConfig,
	}
	return s, nil
}

func (s *Service) TwitterOAuth(ctx context.Context, code, state string) (*types.OAuth2TokenRes, error) {
	authenticate, err := s.oauthClient.TwitterAuthenticate(ctx, code, state)
	if err != nil {
		return nil, err
	}
	_, err = s.repository.SaveUser(ctx, authenticate.Data.Id, authenticate.Data.Name, authenticate.Data.UserName, authenticate.AccessToken, authenticate.RefreshToken)
	if err != nil {
		return nil, err
	}
	accessToken, refreshToken, err := s.jwtConfig.GenerateToken(authenticate.Data.Id, authenticate.Data.Name)
	if err != nil {
		return nil, err
	}

	return &types.OAuth2TokenRes{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		ClientURL:    oauth.ClientURL,
	}, nil
}

func (s *Service) TwitterOAuthApp(ctx context.Context, code string, state string) (*types.OAuth2TokenRes, error) {
	authenticate, err := s.oauthClient.TwitterAuthenticateApp(ctx, code, state)
	if err != nil {
		return nil, err
	}
	_, err = s.repository.SaveUser(ctx, authenticate.Data.Id, authenticate.Data.Name, authenticate.Data.UserName, authenticate.AccessToken, authenticate.RefreshToken)
	if err != nil {
		return nil, err
	}
	accessToken, refreshToken, err := s.jwtConfig.GenerateToken(authenticate.Data.Id, authenticate.Data.Name)
	if err != nil {
		return nil, err
	}

	return &types.OAuth2TokenRes{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		ClientURL:    oauth.ClientURL,
	}, nil
}

func (s *Service) GoogleOAuth(ctx context.Context, code string) {

	authenticate, err := s.oauthClient.GoogleAuthenticate(ctx, code)
	if err != nil {
		return
	}
	fmt.Println(authenticate)
}

func (s *Service) GetStartOAuth2() *types.StartOAuth2SetupRes {
	pkce := s.oauthClient.GetPKCE()
	scopes := s.oauthClient.GetTwitterScope()
	clientID := s.oauthClient.GetTwitterClientID()
	return &types.StartOAuth2SetupRes{
		Twitter: types.OAuth2SetupRes{
			Pkce:           pkce,
			Scopes:         scopes,
			WebRedirectUrl: oauth.TwitterWebCallBackURL,
			AppRedirectUrl: oauth.TwitterAppCallBackURL,
			ClientID:       clientID,
		},
		Apple: types.OAuth2AppleSetupRes{
			Scopes:         []string{"name", "email"},
			ResponseType:   "code",
			ResponseMode:   "query",
			ClientId:       s.oauthClient.GetAppleClientID(),
			WebRedirectUrl: oauth.AppleWebCallBackURL,
			AppRedirectUrl: oauth.AppleAppCallBackURL,
		},
	}
}

func (s *Service) StartOAuth1(_ context.Context) (*oauth.OAuth1Token, error) {
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

func (s *Service) GetTweets(ctx context.Context, token string, id string) (string, error) {
	tweets := s.oauthClient.GetTweets(ctx, token, id)
	return tweets, nil
}

func (s *Service) PostTweet(ctx context.Context, token string, tweet string) string {
	result := s.oauthClient.PostTweet(ctx, token, tweet)

	return result
}

func (s *Service) AppleOAuth(ctx context.Context, code string) (*types.OAuth2TokenRes, error) {
	authenticate, err := s.oauthClient.AppleAuthenticate(ctx, code)
	if err != nil {
		return nil, err
	}
	_, err = s.repository.SaveUser(ctx, authenticate.Data.Id, authenticate.Data.Name, authenticate.Data.UserName, authenticate.AccessToken, authenticate.RefreshToken)
	if err != nil {
		return nil, err
	}
	fmt.Println(authenticate)
	accessToken, refreshToken, err := s.jwtConfig.GenerateToken(authenticate.Data.Id, authenticate.Data.Name)
	if err != nil {
		return nil, err
	}
	return &types.OAuth2TokenRes{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		ClientURL:    oauth.ClientURL,
	}, nil

}

func (s *Service) AppleOAuthNative(ctx context.Context, token string) (*types.OAuth2TokenRes, error) {
	authenticate, err := s.oauthClient.AppleAuthenticateNative(ctx, token)
	if err != nil {
		return nil, err
	}
	_, err = s.repository.SaveUser(ctx, authenticate.Data.Id, authenticate.Data.Name, authenticate.Data.UserName, authenticate.AccessToken, authenticate.RefreshToken)
	if err != nil {
		return nil, err
	}
	accessToken, refreshToken, err := s.jwtConfig.GenerateToken(authenticate.Data.Id, authenticate.Data.Name)
	if err != nil {
		return nil, err
	}
	return &types.OAuth2TokenRes{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		ClientURL:    oauth.ClientURL,
	}, nil

}
