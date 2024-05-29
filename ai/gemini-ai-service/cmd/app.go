package cmd

import (
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/network"
	"gemini-ai-service/service"
)

type App struct {
	cfg        *config.Config
	service    *service.Service
	network    *network.Network
	gemini     *ai.Gemini
	repository *infrastructure.Repository
}

func NewApp(cfg *config.Config) {
	a := &App{cfg: cfg}

	var err error
	if a.repository, err = infrastructure.NewRepository(cfg); err != nil {
		panic(err)
	} else if a.gemini, err = ai.NewGemini(cfg); err != nil {
		panic(err)
	} else if a.service, err = service.NewService(cfg, a.gemini, a.repository); err != nil {
		panic(err)
	} else if a.network, err = network.NewNetwork(cfg, a.service); err != nil {
		panic(err)
	} else {
		a.network.StartServer(a.gemini)

		//go a.network.endServer()
	}
}
