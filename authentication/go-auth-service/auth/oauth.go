package auth

import (
	"context"
	"github.com/dghubble/go-twitter/twitter"
	"github.com/dghubble/oauth1"
	"go-auth-service/config"
)

type TwitterOAuth1 struct {
	OAuth1 *oauth1.Config
	Token  *oauth1.Token
}

func NewOAuth1(cfg *config.Config) *TwitterOAuth1 {
	newConfig := oauth1.NewConfig(
		"consumerKey",
		"consumerSecret",
	)
	newConfig.CallbackURL = TwitterCallBackURL

	token := oauth1.NewToken(
		"accessToken",
		"accessSecret",
	)
	return &TwitterOAuth1{
		OAuth1: newConfig,
		Token:  token,
	}
}

func (t *TwitterOAuth1) GetClient(ctx context.Context) *twitter.Client {
	httpClient := t.OAuth1.Client(ctx, t.Token)
	return twitter.NewClient(httpClient)
}

func (t *TwitterOAuth1) TwitterOAuth1Authenticate(ctx context.Context) {
	_ = t.GetClient(ctx)
}

func (t *TwitterOAuth1) Callback() {
	println("callback")
}
