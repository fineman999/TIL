package network

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func (n *Network) test(g *gin.Context) {
	g.JSON(http.StatusOK, gin.H{"response": "test"})
}
