package config

type ServerConfig struct {
	Port      string
	LocalPort string
	Mode      string
}

type Config struct {
	Server ServerConfig
}

func NewConfig(mode string) *Config {
	return &Config{
		Server: ServerConfig{
			Port:      "8080",
			LocalPort: "8081",
			Mode:      mode,
		},
	}
}
