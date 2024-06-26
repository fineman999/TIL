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
	n.service.TwitterOAuth(ctx, code, state)
	g.JSON(http.StatusOK, gin.H{"response": "test"})
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

	info := n.service.GetPkceInfo()
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
	n.service.CallbackOAuth1(ctx, oauthToken, oauthVerifier)
}
