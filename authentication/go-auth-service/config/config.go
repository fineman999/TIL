package config

import (
	"github.com/naoina/toml"
	"log"
	"os"
)

type Config struct {
	Twitter struct {
		ConsumerKey              string
		ConsumerSecret           string
		AccessToken              string
		TwitterOAuthClientID     string
		TwitterOAuthClientSecret string
		Scope                    []string
	}
	Google struct {
		ClientID     string
		ClientSecret string
	}
	Oauth1 struct {
		ConsumerKey    string
		ConsumerSecret string
		AccessToken    string
		AccessSecret   string
	}
	Jwt struct {
		SecretKey string
	}
	Mongo struct {
		Username string
		Password string
	}
	Apple struct {
		ClientID string
		KeyID    string
		TeamID   string
		Secret   string
	}
}

func NewConfig(path string) *Config {
	c := new(Config)

	if file, err := os.Open(path); err != nil {
		panic(err)
	} else {
		defer func(file *os.File) {
			err := file.Close()
			if err != nil {
				panic(err)
			}
		}(file)

		if err = toml.NewDecoder(file).Decode(c); err != nil {
			panic(err)
		} else {
			appleSecretPath := "./AuthKey_45SHHKVMNM.p8"
			content, err := os.ReadFile(appleSecretPath)
			if err != nil {
				log.Fatal(err)
			}
			c.Apple.Secret = string(content)
			return c
		}
	}
}
