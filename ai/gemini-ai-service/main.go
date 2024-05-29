package main

import (
	"flag"
	"gemini-ai-service/cmd"
	"gemini-ai-service/config"
)

var configFlag = flag.String("config", "./config.toml", "config path")

func main() {
	flag.Parse()

	cfg := config.NewConfig(*configFlag)

	cmd.NewApp(cfg)

}
