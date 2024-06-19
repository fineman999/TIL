package auth

import (
	"context"
	"go-auth-service/config"
	"golang.org/x/oauth2"
	"time"
)

const (
	CallBackURL = "https://chan-factory.store/login/oauth2/code/x"
)

var EndPoint = oauth2.Endpoint{
	AuthURL:  "https://twitter.com/i/oauth2/authorize",
	TokenURL: "https://api.twitter.com/2/oauth2/token",
}

type TwitterAuth struct {
	OAuthConf *oauth2.Config
}

func NewAuth(cfg *config.Config) *TwitterAuth {
	oauthConf := &oauth2.Config{
		ClientID:     cfg.Twitter.TwitterOAuthClientID,
		ClientSecret: cfg.Twitter.TwitterOAuthClientSecret,
		RedirectURL:  CallBackURL,
		Scopes:       []string{"users.read", "tweet.read", "follows.read", "follows.write"},
		Endpoint:     EndPoint,
	}
	return &TwitterAuth{OAuthConf: oauthConf}
}

func (t *TwitterAuth) GetAuthURL() string {
	return t.OAuthConf.AuthCodeURL("state")
}
func (t *TwitterAuth) GetAuthURLWithState(state string) string {
	return t.OAuthConf.AuthCodeURL(state)
}

func (t *TwitterAuth) GetAuthURLWithStateAndPrompt(state string, prompt string) string {
	return t.OAuthConf.AuthCodeURL(state, oauth2.SetAuthURLParam("prompt", prompt))
}

func (t *TwitterAuth) GenerateVerifier() string {
	return ""
}

func (t *TwitterAuth) GetToken(code string) (*oauth2.Token, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	token, err := t.OAuthConf.Exchange(ctx, code)
	if err != nil {
		return nil, err
	}
	return token, nil
}

func (t *TwitterAuth) GetUserInfo(token *oauth2.Token) (string, error) {
	return "", nil
}

func (t *TwitterAuth) RefreshToken(token *oauth2.Token) (*oauth2.Token, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	tokenSource := t.OAuthConf.TokenSource(ctx, token)
	token, err := tokenSource.Token()
	if err != nil {
		return nil, err
	}
	return token, nil
}

func (t *TwitterAuth) RevokeToken(token *oauth2.Token) error {
	return nil
}
