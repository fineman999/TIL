package oauth

import (
	"context"
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"go-auth-service/config"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"io"
	"time"
)

const (
	TwitterCallBackURL = "http://127.0.0.1:8080/login/oauth2/code/twitter"
	GoogleCallBackURL  = "http://127.0.0.1:8080/login/oauth2/code/google"
	ClientURL          = "http://localhost:3000"

	// 인증 후 유저 정보를 가져오기 위한 API
	GoogleUserInfoAPIEndpoint  = "https://www.googleapis.com/oauth2/v3/userinfo"
	TwitterUserInfoAPIEndpoint = "https://api.twitter.com/2/users/me"
	ScopeEmail                 = "https://www.googleapis.com/auth/userinfo.email"
	ScopeProfile               = "https://www.googleapis.com/auth/userinfo.profile"
)

var EndPoint = oauth2.Endpoint{
	AuthURL:  "https://twitter.com/i/oauth2/authorize",
	TokenURL: "https://api.twitter.com/2/oauth2/token",
}

type Pkce struct {
	CodeVerifier        string `json:"code_verifier"`
	CodeChallenge       string `json:"code_challenge"`
	CodeChallengeMethod string `json:"code_challenge_method"`
	State               string `json:"state"`
}

// TODO: Redis로 관리 필요
var ListPkce = make(map[string]*Pkce)

type User struct {
	Data UserDetail `json:"data"`
}

type UserDetail struct {
	Id       string `json:"id"`
	UserName string `json:"username"`
	Name     string `json:"name"`
}

type OAuth struct {
	twitterOAuth *oauth2.Config
	googleOAuth  *oauth2.Config
}

func NewAuth(cfg *config.Config) *OAuth {
	oauthConf := &oauth2.Config{
		ClientID:     cfg.Twitter.TwitterOAuthClientID,
		ClientSecret: cfg.Twitter.TwitterOAuthClientSecret,
		RedirectURL:  TwitterCallBackURL,
		Scopes:       cfg.Twitter.Scope,
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
		twitterOAuth: oauthConf,
		googleOAuth:  googleOAuth,
	}
}

func (t *OAuth) getTwitterAuthURLWithStateAndPrompts(state string) string {
	authUrlParams := make([]oauth2.AuthCodeOption, 0)
	authUrlParams = append(authUrlParams, oauth2.SetAuthURLParam("user.fields", "profile_image_url"))
	return t.twitterOAuth.AuthCodeURL(state, authUrlParams...)
}

func (t *OAuth) GetTwitterClientID() string {
	return t.twitterOAuth.ClientID
}
func (t *OAuth) GetTwitterScope() []string {
	return t.twitterOAuth.Scopes
}

func (t *OAuth) GetToken(code string) (*oauth2.Token, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	token, err := t.twitterOAuth.Exchange(ctx, code)
	if err != nil {
		return nil, err
	}
	return token, nil
}
func (t *OAuth) RefreshToken(token *oauth2.Token) (*oauth2.Token, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	tokenSource := t.twitterOAuth.TokenSource(ctx, token)
	token, err := tokenSource.Token()
	if err != nil {
		return nil, err
	}
	return token, nil
}

func (t *OAuth) TwitterAuthenticate(ctx context.Context, code string, state string) (*UserDetail, error) {

	if _, ok := ListPkce[state]; !ok {
		return nil, fmt.Errorf("state not found")
	}
	pkce := ListPkce[state]
	token, err := t.twitterOAuth.Exchange(ctx, code, oauth2.VerifierOption(pkce.CodeVerifier))
	if err != nil {
		return nil, fmt.Errorf("failed to exchange token: %w", err)
	}

	client := t.twitterOAuth.Client(ctx, token)
	userInfoRes, err := client.Get(TwitterUserInfoAPIEndpoint)
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

	//source := t.twitterOAuth.TokenSource(ctx, token)
	//token, err = source.Token()
	//if err != nil {
	//	return nil, fmt.Errorf("failed to get token: %w", err)
	//}
	return &authUser.Data, nil
}

func (t *OAuth) GoogleAuthenticate(ctx context.Context, code string) (*User, error) {

	token, err := t.googleOAuth.Exchange(ctx, code)
	if err != nil {
		return nil, fmt.Errorf("failed to exchange token: %w", err)
	}

	client := t.googleOAuth.Client(ctx, token)
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

	source := t.googleOAuth.TokenSource(ctx, token)
	token, err = source.Token()
	if err != nil {
		return nil, fmt.Errorf("failed to get token: %w", err)
	}
	return &authUser, nil
}

func (t *OAuth) GetPKCE() *Pkce {
	verifier := oauth2.GenerateVerifier()
	challengeFromVerifier := oauth2.S256ChallengeFromVerifier(verifier)
	state := RandToken()
	p := &Pkce{
		CodeVerifier:        verifier,
		CodeChallenge:       challengeFromVerifier,
		CodeChallengeMethod: "S256",
		State:               state,
	}
	ListPkce[state] = p
	return p
}

// 랜덤 state 생성기
func RandToken() string {
	// Create a byte slice of 32 bytes.
	state := make([]byte, 32)
	// Read random bytes into the slice.
	if _, err := rand.Read(state); err != nil {
		// If an error occurs, panic.
		panic(err)
	}
	// Return the base64 URL-encoded string.
	return base64.RawURLEncoding.EncodeToString(state)
}
