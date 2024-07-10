package types

import "go-auth-service/oauth"

type StartOAuth2SetupRes struct {
	Twitter OAuth2SetupRes      `json:"twitter"`
	Apple   OAuth2AppleSetupRes `json:"apple"`
}

type OAuth2SetupRes struct {
	Pkce           *oauth.Pkce `json:"pkce"`
	WebRedirectUrl string      `json:"web_redirect_url"`
	AppRedirectUrl string      `json:"app_redirect_url"`
	Scopes         []string    `json:"scopes"`
	ClientID       string      `json:"client_id"`
}

type OAuth2AppleSetupRes struct {
	Scopes         []string `json:"scopes"`
	ResponseType   string   `json:"response_type"`
	ResponseMode   string   `json:"response_mode"`
	ClientId       string   `json:"client_id"`
	WebRedirectUrl string   `json:"web_redirect_url"`
	AppRedirectUrl string   `json:"app_redirect_url"`
}

type OAuth2TokenRes struct {
	AccessToken  string `json:"access_token"`
	RefreshToken string `json:"refresh_token"`
	ClientURL    string `json:"client_url"`
}

type TokenInfo struct {
	AccessToken string `json:"access_token"`
	Id          string
}
