package auth

import (
	"context"
	"encoding/json"
	"fmt"
	"go-auth-service/config"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"io"
	"time"
)

const (
	TwitterCallBackURL = "https://chan-factory.store/login/oauth2/code/x"
	GoogleCallBackURL  = "http://localhost:8080/login/oauth2/code/google"

	// 인증 후 유저 정보를 가져오기 위한 API
	GoogleUserInfoAPIEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo"
	ScopeEmail                = "https://www.googleapis.com/auth/userinfo.email"
	ScopeProfile              = "https://www.googleapis.com/auth/userinfo.profile"
)

var EndPoint = oauth2.Endpoint{
	AuthURL:  "https://twitter.com/i/oauth2/authorize",
	TokenURL: "https://api.twitter.com/2/oauth2/token",
}

type User struct {
	Email string `json:"email"`
	Name  string `json:"name"`
}

type OAuth struct {
	TwitterOAuth *oauth2.Config
	GoogleOAuth  *oauth2.Config
}

func NewAuth(cfg *config.Config) *OAuth {
	oauthConf := &oauth2.Config{
		ClientID:     cfg.Twitter.TwitterOAuthClientID,
		ClientSecret: cfg.Twitter.TwitterOAuthClientSecret,
		RedirectURL:  TwitterCallBackURL,
		Scopes:       []string{"users.read", "tweet.read", "follows.read", "follows.write"},
		Endpoint:     EndPoint,
	}
	googleOAuth := &oauth2.Config{
		ClientID:     cfg.Google.ClientID,
		ClientSecret: cfg.Google.ClientSecret,
		RedirectURL:  GoogleCallBackURL,
		Scopes:       []string{ScopeEmail, ScopeProfile},
		Endpoint:     google.Endpoint,
	}
	return &OAuth{
		TwitterOAuth: oauthConf,
		GoogleOAuth:  googleOAuth,
	}
}
func (t *OAuth) GetAuthURLWithState(state string) string {
	return t.TwitterOAuth.AuthCodeURL(state)
}

func (t *OAuth) GetAuthURLWithStateAndPrompt(state string, prompt string) string {
	return t.TwitterOAuth.AuthCodeURL(state, oauth2.SetAuthURLParam("prompt", prompt))
}

func (t *OAuth) GetToken(code string) (*oauth2.Token, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	token, err := t.TwitterOAuth.Exchange(ctx, code)
	if err != nil {
		return nil, err
	}
	return token, nil
}
func (t *OAuth) RefreshToken(token *oauth2.Token) (*oauth2.Token, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	tokenSource := t.TwitterOAuth.TokenSource(ctx, token)
	token, err := tokenSource.Token()
	if err != nil {
		return nil, err
	}
	return token, nil
}

func (t *OAuth) GoogleAuthenticate(ctx context.Context, code string) (*User, error) {

	token, err := t.GoogleOAuth.Exchange(ctx, code)
	if err != nil {
		return nil, fmt.Errorf("failed to exchange token: %w", err)
	}

	client := t.GoogleOAuth.Client(ctx, token)
	userInfoRes, err := client.Get(GoogleUserInfoAPIEndpoint)
	if err != nil {
		return nil, fmt.Errorf("failed to get user info: %w", err)
	}

	defer userInfoRes.Body.Close()
	userInfo, err := io.ReadAll(userInfoRes.Body)
	if err != nil {
		return nil, fmt.Errorf("failed to read user info: %w", err)
	}

	var authUser User
	if err := json.Unmarshal(userInfo, &authUser); err != nil {
		return nil, fmt.Errorf("failed to unmarshal user info: %w", err)
	}

	source := t.GoogleOAuth.TokenSource(ctx, token)
	token, err = source.Token()
	if err != nil {
		return nil, fmt.Errorf("failed to get token: %w", err)
	}
	return &authUser, nil
}
