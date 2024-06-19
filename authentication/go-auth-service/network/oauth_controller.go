package network

import (
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
)

func (n *Network) twitterOAuth(g *gin.Context) {
	log.Println("twitterOAuth")
	log.Println(g.Request.URL.Query())

	//code := g.Query("code")
	log.Println(g.Request.URL)
	log.Println("body : ", g.Request.Body)
	g.JSON(http.StatusOK, gin.H{"response": "test"})
}
