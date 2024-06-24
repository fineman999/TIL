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
