package config

import (
	"github.com/naoina/toml"
	"os"
)

type Config struct {
	Slack struct {
		Token   string
		Channel string
	}
	Discord struct {
		Token   string
		Channel string
	}
}

func NewConfig() *Config {
	path := "./creational-patterns/factory-method/config/config.toml"
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
