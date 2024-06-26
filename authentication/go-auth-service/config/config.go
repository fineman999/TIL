package config

import (
	"github.com/naoina/toml"
	"os"
)

type Config struct {
	Twitter struct {
		ConsumerKey              string
		ConsumerSecret           string
		AccessToken              string
		TwitterOAuthClientID     string
		TwitterOAuthClientSecret string
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
			return c
		}
	}
}
