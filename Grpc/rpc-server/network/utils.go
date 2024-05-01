package network

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

func (n *Network) verifyLogin() gin.HandlerFunc {
	return func(g *gin.Context) {
		token := getAuthToken(g)

		if token == "" {
			g.AbortWithStatusJSON(http.StatusUnauthorized, gin.H{"error": "Unauthorized"})
			return
		}
		if _, err := n.gRPCClient.VerifyAuth(token); err != nil {
			g.AbortWithStatusJSON(http.StatusUnauthorized, gin.H{"error": err.Error(), "message": "Unauthorized"})
			return
		}

		g.Next()
	}
}

func getAuthToken(g *gin.Context) string {
	var token string

	authToken := g.GetHeader("Authorization")

	authSlice := strings.Split(authToken, " ")

	if len(authSlice) == 2 {
		token = authSlice[1]
	}

	return token
}
