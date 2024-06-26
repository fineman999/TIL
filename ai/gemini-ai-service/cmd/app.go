package cmd

import (
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/gcs"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/network"
	"gemini-ai-service/service"
	"gemini-ai-service/slack"
)

type App struct {
	cfg        *config.Config
	service    *service.Service
	network    *network.Network
	gemini     *ai.Gemini
	slack      *slack.Slack
	repository *infrastructure.Repository
	vertex     *ai.Vertex
	storage    *gcs.Storage
}

func NewApp(cfg *config.Config) {
	a := &App{cfg: cfg}

	var err error
	if a.repository, err = infrastructure.NewRepository(cfg); err != nil {
		panic(err)
	} else if a.storage, err = gcs.NewStorage(cfg); err != nil {
		panic(err)
	} else if a.slack, err = slack.NewSlack(cfg); err != nil {
		panic(err)
	} else if a.gemini, err = ai.NewGemini(cfg, a.slack); err != nil {
		panic(err)
	} else if a.vertex, err = ai.NewVertex(cfg); err != nil {
		panic(err)
	} else if a.service, err = service.NewService(cfg,
		a.gemini,
		a.repository,
		a.slack,
		a.vertex,
		a.storage); err != nil {
		panic(err)
	} else if a.network, err = network.NewNetwork(cfg, a.service); err != nil {
		panic(err)
	} else {
		a.network.StartServer(a.gemini, a.vertex, a.storage)
	}
}
