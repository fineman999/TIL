package config

import (
	"github.com/naoina/toml"
	"os"
)

type Config struct {
	Gemini struct {
		Key string
	}
	Slack struct {
		Token   string
		Channel string
	}
	Vertex struct {
		Location string
		Project  string
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

func (c *Config) SetEnvVarFromJSON() error {
	_, err := os.Stat("vertex-api-key.json")
	if err != nil {
		return err
	}
	err = os.Setenv("GOOGLE_APPLICATION_CREDENTIALS", "vertex-api-key.json")
	if err != nil {
		return err
	}
	return nil
}
