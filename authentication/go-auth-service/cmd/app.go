package cmd

import (
	"go-auth-service/config"
	"go-auth-service/network"
	"go-auth-service/repository"
	"go-auth-service/service"
)

type App struct {
	cfg        *config.Config
	service    *service.Service
	network    *network.Network
	repository *repository.Repository
}

func NewApp(cfg *config.Config, port string) {
	a := &App{cfg: cfg}
	var err error
	if a.repository, err = repository.NewRepository(cfg); err != nil {
		panic(err)
	} else if a.service, err = service.NewService(cfg,
		a.repository,
	); err != nil {
		panic(err)
	} else if a.network, err = network.NewNetwork(cfg, a.service); err != nil {
		panic(err)
	} else {
		a.network.StartServer(port)
	}
}
