package network

import (
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
)

func (n *Network) twitterOAuth(g *gin.Context) {
	log.Println("twitterOAuth")
	log.Println(g.Request.URL.Query())

	code := g.Query("code")
	state := g.Query("state")
	ctx := g.Request.Context()
	oAuth, err := n.service.TwitterOAuth(ctx, code, state)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"response": err.Error()})
		return
	}
	//clientURL := oAuth.ClientURL
	accessToken := oAuth.AccessToken
	refreshToken := oAuth.RefreshToken
	g.SetCookie("access_token", accessToken, 3600, "/", "127.0.0.1", false, false) // 1 hour expiry

	// Set the refresh token as an HttpOnly cookie (not accessible by JavaScript)
	g.SetCookie("refresh_token", refreshToken, 604800, "/", "127.0.0.1", false, true) // 1 week expiry, HttpOnly
	g.SetSameSite(http.SameSiteNoneMode)
	// Redirect to the client URL
	g.Redirect(http.StatusMovedPermanently, "http://127.0.0.1:3000")
}

func (n *Network) googleOAuth(g *gin.Context) {
	log.Println("test")
	log.Println(g.Request.URL.Query())

	code := g.Query("code")
	ctx := g.Request.Context()

	n.service.GoogleOAuth(ctx, code)

	log.Println(g.Request.URL)
	log.Println("body : ", g.Request.Body)
	g.JSON(http.StatusOK, gin.H{"response": "test"})
}

func (n *Network) getPkceInfo(g *gin.Context) {
	log.Println("getPkceInfo")
	info := n.service.GetStartOAuth2()
	g.JSON(http.StatusOK, gin.H{"response": info})
}

func (n *Network) startOauth1(g *gin.Context) {

	ctx := g.Request.Context()
	auth1, err := n.service.StartOAuth1(ctx)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"response": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": auth1})
}

func (n *Network) callbackOAuth1(g *gin.Context) {
	oauthToken := g.Query("oauth_token")
	oauthVerifier := g.Query("oauth_verifier")
	ctx := g.Request.Context()
	auth1, err := n.service.CallbackOAuth1(ctx, oauthToken, oauthVerifier)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"response": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": auth1})
}

func (n *Network) getTweets(g *gin.Context) {
	token := g.Query("token")
	id := g.Query("id")
	ctx := g.Request.Context()
	tweets, err := n.service.GetTweets(ctx, token, id)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"response": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": tweets})
}

func (n *Network) postTweet(g *gin.Context) {
	token := g.Query("token")
	tweet := g.Query("tweet")
	ctx := g.Request.Context()
	result := n.service.PostTweet(ctx, token, tweet)
	g.JSON(http.StatusOK, gin.H{"response": result})
}
