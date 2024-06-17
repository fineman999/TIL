package main

import (
	"flag"
	"go-auth-service/cmd"
	"go-auth-service/config"

	"log"
)

var configFlag = flag.String("config", "./config.toml", "config path")

func main() {
	port := flag.String("port", "8080", "server port")
	flag.Parse()

	log.Println("config path: ", *configFlag)
	cfg := config.NewConfig(*configFlag)

	cmd.NewApp(cfg, *port)

}
