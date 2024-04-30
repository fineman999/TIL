package main

import (
	"flag"
	"rpc-server/cmd"
	"rpc-server/config"
	"rpc-server/gRPC/server"
)

// flag 패키지는 Go 언어에서 커맨드라인 인자를 처리하는데 사용
// 1. 플래그의 이름("config")
// 2. 플래그의 기본값("./config.toml")
// 3. 플래그에 대한 설명("config path")
var configFlag = flag.String("config", "./config.toml", "config path")

func main() {
	flag.Parse()

	cfg := config.NewConfig(*configFlag)

	err := server.NewGRPCServer(cfg)
	if err != nil {
		panic(err)
	}
	cmd.NewApp(cfg)
}
