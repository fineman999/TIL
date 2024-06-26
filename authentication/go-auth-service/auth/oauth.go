package auth

import (
	"context"
	"github.com/dghubble/go-twitter/twitter"
	"github.com/dghubble/oauth1"
	"go-auth-service/config"
	"net/url"
)

type TwitterOAuth1 struct {
	OAuth1 *oauth1.Config
	Token  *oauth1.Token
}

const (
	TwitterRequestTokenURL = "https://twitter.com/oauth/request_token"
	TwitterAccessTokenURL  = "https://twitter.com/oauth/access_token"
	ClientCallbackURL      = "http://127.0.0.1:3000/authpage"
	TwitterRedirectURL     = "https://twitter.com/oauth/authenticate?oauth_token="
	AuthorizeURL           = "https://twitter.com/oauth/authenticate"
)

func NewOAuth1(cfg *config.Config) *TwitterOAuth1 {
	newConfig := oauth1.NewConfig(
		cfg.Oauth1.ConsumerKey,
		cfg.Oauth1.ConsumerSecret,
	)
	newConfig.CallbackURL = ClientCallbackURL
	newConfig.Endpoint = oauth1.Endpoint{
		RequestTokenURL: TwitterRequestTokenURL,
		AccessTokenURL:  TwitterAccessTokenURL,
		AuthorizeURL:    AuthorizeURL,
	}
	token := oauth1.NewToken(
		cfg.Oauth1.AccessToken,
		cfg.Oauth1.AccessSecret,
	)
	return &TwitterOAuth1{
		OAuth1: newConfig,
		Token:  token,
	}
}

type OAuth1Token struct {
	OAuthRequestToken       string `json:"oauthRequestToken"`
	OAuthRequestTokenSecret string `json:"oauthRequestTokenSecret"`
	RedirectURL             string `json:"redirectUrl"`
}

func (t *TwitterOAuth1) GetClient(ctx context.Context) *twitter.Client {
	httpClient := t.OAuth1.Client(ctx, t.Token)
	return twitter.NewClient(httpClient)

}

func (t *TwitterOAuth1) GetOAuthInfo(ctx context.Context) (*OAuth1Token, error) {
	requestToken, requestTokenSecret, err := t.OAuth1.RequestToken()

	if err != nil {
		return nil, err
	}
	urlCheck, err := t.OAuth1.AuthorizationURL(requestToken)
	if err != nil {
		return nil, err
	}
	encodedURL := urlCheck.String() + "&oauth_callback=" + url.QueryEscape(ClientCallbackURL)
	return &OAuth1Token{
		OAuthRequestToken:       requestToken,
		OAuthRequestTokenSecret: requestTokenSecret,
		RedirectURL:             encodedURL,
	}, nil
}

func (t *TwitterOAuth1) VerifyOAuth(ctx context.Context, token string, verifier string) {
	panic("implement me")
}
