package middleware

import (
	"github.com/gin-gonic/gin"
)

func NewAuthMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		authHeader := c.GetHeader("Authorization")
		if authHeader == "" {
			c.JSON(401, gin.H{"error": "Authorization header required"})
			c.Abort()
			return
		}
		// 실제로는 JWT나 다른 인증 메커니즘을 구현해야 함
		c.Next()
	}
}
