package types

import "go-auth-service/oauth"

type StartOAuth2SetupRes struct {
	Pkce        *oauth.Pkce `json:"pkce"`
	RedirectUrl string      `json:"redirect_url"`
	Scopes      []string    `json:"scopes"`
	ClientID    string      `json:"client_id"`
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
