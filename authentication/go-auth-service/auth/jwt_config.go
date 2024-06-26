package auth

import (
	"fmt"
	"github.com/golang-jwt/jwt/v5"
	"go-auth-service/config"
	"time"
)

type JwtConfig struct {
	secretKey string
}

// Claims defines the structure of JWT claims
type Claims struct {
	UserID string `json:"userId"`
	Email  string `json:"email"`
	jwt.RegisteredClaims
}

func NewJwtConfig(cfg *config.Config) *JwtConfig {
	return &JwtConfig{
		secretKey: cfg.Jwt.SecretKey,
	}
}

// GenerateToken generates an access token and a refresh token
func (j JwtConfig) GenerateToken(userID, email string) (string, string, error) {
	// Define the expiration times
	accessTokenExpiration := time.Now().Add(15 * time.Minute)
	refreshTokenExpiration := time.Now().Add(7 * 24 * time.Hour)

	// Create the claims
	accessTokenClaims := Claims{
		UserID: userID,
		Email:  email,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(accessTokenExpiration),
		},
	}

	refreshTokenClaims := Claims{
		UserID: userID,
		Email:  email,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(refreshTokenExpiration),
		},
	}

	// Create the tokens
	accessToken := jwt.NewWithClaims(jwt.SigningMethodHS256, accessTokenClaims)
	refreshToken := jwt.NewWithClaims(jwt.SigningMethodHS256, refreshTokenClaims)

	// Sign the tokens
	signedAccessToken, err := accessToken.SignedString(j.secretKey)
	if err != nil {
		return "", "", err
	}

	signedRefreshToken, err := refreshToken.SignedString(j.secretKey)
	if err != nil {
		return "", "", err
	}

	return signedAccessToken, signedRefreshToken, nil
}

// ValidateToken validates the token and returns the claims
func (j JwtConfig) ValidateToken(tokenStr string) (*Claims, error) {
	token, err := jwt.ParseWithClaims(tokenStr, &Claims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return j.secretKey, nil
	})

	if err != nil {
		return nil, err
	}

	if claims, ok := token.Claims.(*Claims); ok && token.Valid {
		return claims, nil
	}

	return nil, fmt.Errorf("invalid token")
}
