package cmd

import (
	"rpc-server/config"
	"rpc-server/gRPC/client"
	"rpc-server/network"
	"rpc-server/repository"
	"rpc-server/service"
)

type App struct {
	cfg        *config.Config
	service    *service.Service
	repository *repository.Repository
	network    *network.Network
	gRPCClient *client.GRPCClient
}

func NewApp(cfg *config.Config) {
	a := &App{cfg: cfg}

	var err error
	if a.gRPCClient, err = client.NewGRPCClient(cfg); err != nil {
		panic(err)
	} else if a.repository, err = repository.NewRepository(cfg, a.gRPCClient); err != nil {
		panic(err)
	} else if a.service, err = service.NewService(cfg, a.repository); err != nil {
		panic(err)
	} else if a.network, err = network.NewNetwork(cfg, a.service, a.gRPCClient); err != nil {
		panic(err)
	} else {
		a.network.StartServer()
	}
}
