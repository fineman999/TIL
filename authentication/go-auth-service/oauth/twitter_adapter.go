package oauth

import (
	"context"
	"golang.org/x/oauth2"
	"io"
	"strings"
)

const (
	TwitterGetTweetsURL = "https://api.twitter.com/2/tweets"
)

func (t *OAuth) GetTweets(ctx context.Context, tokenAccessToken, id string) string {
	token := &oauth2.Token{
		AccessToken: tokenAccessToken,
	}
	client := t.twitterOAuth.Client(ctx, token)

	tweets, err := client.Get(TwitterGetTweetsURL + "?ids=" + id)
	if err != nil {
		return ""
	}
	defer tweets.Body.Close()
	tweetsData, err := io.ReadAll(tweets.Body)
	if err != nil {
		return ""
	}
	return string(tweetsData)
}

func (t *OAuth) PostTweet(ctx context.Context, tokenAccessToken, tweet string) string {
	token := &oauth2.Token{
		AccessToken: tokenAccessToken,
	}
	client := t.twitterOAuth.Client(ctx, token)

	tweetData := strings.NewReader(`{"text":"` + tweet + `"}`)
	tweetRes, err := client.Post(TwitterGetTweetsURL, "application/json", tweetData)
	if err != nil {
		return ""
	}
	defer tweetRes.Body.Close()

	tweetResponse, err := io.ReadAll(tweetRes.Body)
	if err != nil {
		return ""
	}

	return string(tweetResponse)
}
