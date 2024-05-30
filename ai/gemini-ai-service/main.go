package main

import (
	"flag"
	"gemini-ai-service/cmd"
	"gemini-ai-service/config"
	"log"
)

var configFlag = flag.String("config", "./config.toml", "config path")

func main() {
	flag.Parse()

	log.Println("config path: ", *configFlag)
	cfg := config.NewConfig(*configFlag)

	cmd.NewApp(cfg)

}
