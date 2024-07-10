package oauth

import (
	"context"
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/Timothylock/go-signin-with-apple/apple"
	"go-auth-service/config"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"io"
	"log"
	"time"
)

const (
	TwitterWebCallBackURL = "https://www.chan-factory.store/login/oauth2/code/twitter"
	TwitterAppCallBackURL = "https://www.chan-factory.store/login/oauth2/code/twitter/app"
	//TwitterAppCallBackURL = "http://127.0.0.1:8080/login/oauth2/code/twitter/app"
	//TwitterWebCallBackURL = "http://127.0.0.1:8080/login/oauth2/code/twitter"
	GoogleCallBackURL   = "http://127.0.0.1:8080/login/oauth2/code/google"
	AppleAppCallBackURL = "https://www.chan-factory.store/login/oauth2/code/apple/app"
	AppleWebCallBackURL = "https://www.chan-factory.store/login/oauth2/code/apple"
	ClientURL           = "http://localhost:3000"

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
	Data         UserDetail `json:"data"`
	AccessToken  string     `json:"access_token"`
	RefreshToken string     `json:"refresh_token"`
}

type UserDetail struct {
	Id       string `json:"id"`
	UserName string `json:"username"`
	Name     string `json:"name"`
}

type appleOAuth struct {
	secret   string
	clientId string
}

type OAuth struct {
	twitterOAuth    *oauth2.Config
	googleOAuth     *oauth2.Config
	twitterAppOAuth *oauth2.Config
	appleOAuth      *appleOAuth
}

func NewAuth(cfg *config.Config) *OAuth {
	oauthConf := &oauth2.Config{
		ClientID:     cfg.Twitter.TwitterOAuthClientID,
		ClientSecret: cfg.Twitter.TwitterOAuthClientSecret,
		RedirectURL:  TwitterWebCallBackURL,
		Scopes:       cfg.Twitter.Scope,
		Endpoint:     EndPoint,
	}
	twitterOAuthConf := &oauth2.Config{
		ClientID:     cfg.Twitter.TwitterOAuthClientID,
		ClientSecret: cfg.Twitter.TwitterOAuthClientSecret,
		RedirectURL:  TwitterAppCallBackURL,
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
	secret, err := apple.GenerateClientSecret(cfg.Apple.Secret, cfg.Apple.TeamID, cfg.Apple.ClientID, cfg.Apple.KeyID)
	if err != nil {
		log.Fatal(err)
	}
	return &OAuth{
		twitterOAuth:    oauthConf,
		googleOAuth:     googleOAuth,
		twitterAppOAuth: twitterOAuthConf,
		appleOAuth: &appleOAuth{
			secret:   secret,
			clientId: cfg.Apple.ClientID,
		},
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

func (t *OAuth) AppleAuthenticate(ctx context.Context, code string) (*User, error) {
	// Generate a new validation client
	client := apple.New()

	vReq := apple.AppValidationTokenRequest{
		ClientID:     t.appleOAuth.clientId,
		ClientSecret: t.appleOAuth.secret,
		Code:         code,
	}

	var resp apple.ValidationResponse

	// Do the verification
	err := client.VerifyAppToken(ctx, vReq, &resp)
	if err != nil {
		fmt.Println("error verifying: " + err.Error())
		return nil, err
	}

	if resp.Error != "" {
		return nil, fmt.Errorf("apple returned an error: %s - %s\n", resp.Error, resp.ErrorDescription)
	}
	// Get the unique user ID
	unique, err := apple.GetUniqueID(resp.IDToken)
	if err != nil {
		fmt.Println("failed to get unique ID: " + err.Error())
		return nil, err
	}

	fmt.Println("unique id: " + unique)
	fmt.Println("access token: " + resp.IDToken)
	// Get the email
	claim, err := apple.GetClaims(resp.IDToken)
	if err != nil {
		fmt.Println("failed to get claims: " + err.Error())
		return nil, err
	}

	email := (*claim)["email"]
	emailVerified := (*claim)["email_verified"]
	isPrivateEmail := (*claim)["is_private_email"]

	// Voila!
	fmt.Println(unique)
	fmt.Println(email)
	fmt.Println(emailVerified)
	fmt.Println(isPrivateEmail)
	return &User{
		AccessToken:  resp.AccessToken,
		RefreshToken: resp.RefreshToken,
		Data: UserDetail{
			//UserName: email.(string),
			//Name:     email.(string),
			Id: unique,
		},
	}, nil

}

func (t *OAuth) TwitterAuthenticateApp(ctx context.Context, code string, state string) (*User, error) {
	if _, ok := ListPkce[state]; !ok {
		return nil, fmt.Errorf("state not found")
	}
	pkce := ListPkce[state]
	token, err := t.twitterAppOAuth.Exchange(ctx, code, oauth2.VerifierOption(pkce.CodeVerifier))
	if err != nil {
		return nil, fmt.Errorf("failed to exchange token: %w", err)
	}

	client := t.twitterAppOAuth.Client(ctx, token)

	userInfoRes, err := client.Get(TwitterUserInfoAPIEndpoint + "?user.fields=profile_image_url,created_at")
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
	authUser.AccessToken = token.AccessToken
	authUser.RefreshToken = token.RefreshToken
	return &authUser, nil
}

func (t *OAuth) TwitterAuthenticate(ctx context.Context, code string, state string) (*User, error) {

	if _, ok := ListPkce[state]; !ok {
		return nil, fmt.Errorf("state not found")
	}
	pkce := ListPkce[state]
	token, err := t.twitterOAuth.Exchange(ctx, code, oauth2.VerifierOption(pkce.CodeVerifier))
	if err != nil {
		return nil, fmt.Errorf("failed to exchange token: %w", err)
	}

	client := t.twitterOAuth.Client(ctx, token)

	userInfoRes, err := client.Get(TwitterUserInfoAPIEndpoint + "?user.fields=profile_image_url,created_at")
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
	authUser.AccessToken = token.AccessToken
	authUser.RefreshToken = token.RefreshToken
	return &authUser, nil
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

func (t *OAuth) GetAppleClientID() string {
	return t.appleOAuth.clientId
}

func (t *OAuth) AppleAuthenticateNative(ctx context.Context, token string) (*User, error) {
	// Generate a new validation client
	client := apple.New()

	vReq := apple.AppValidationTokenRequest{
		ClientID:     t.appleOAuth.clientId,
		ClientSecret: t.appleOAuth.secret,
		Code:         token,
	}

	var resp apple.ValidationResponse

	// Do the verification
	err := client.VerifyAppToken(ctx, vReq, &resp)
	if err != nil {
		fmt.Println("error verifying: " + err.Error())
		return nil, err
	}

	if resp.Error != "" {
		return nil, fmt.Errorf("apple returned an error: %s - %s\n", resp.Error, resp.ErrorDescription)
	}
	// Get the unique user ID
	unique, err := apple.GetUniqueID(resp.IDToken)
	if err != nil {
		fmt.Println("failed to get unique ID: " + err.Error())
		return nil, err
	}

	fmt.Println("unique id: " + unique)
	fmt.Println("access token: " + resp.IDToken)
	// Get the email
	claim, err := apple.GetClaims(resp.IDToken)
	if err != nil {
		fmt.Println("failed to get claims: " + err.Error())
		return nil, err
	}

	email := (*claim)["email"]
	emailVerified := (*claim)["email_verified"]
	isPrivateEmail := (*claim)["is_private_email"]

	// Voila!
	fmt.Println(unique)
	fmt.Println(email)
	fmt.Println(emailVerified)
	fmt.Println(isPrivateEmail)
	return &User{
		AccessToken:  resp.AccessToken,
		RefreshToken: resp.RefreshToken,
		Data: UserDetail{
			//UserName: email.(string),
			//Name:     email.(string),
			Id: unique,
		},
	}, nil

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
