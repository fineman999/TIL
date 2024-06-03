package service

import (
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/slack"
)

type Service struct {
	cfg        *config.Config
	gemini     *ai.Gemini
	vertex     *ai.Vertex
	repository *infrastructure.Repository
	slack      *slack.Slack
}

func NewService(cfg *config.Config,
	gemini *ai.Gemini,
	repository *infrastructure.Repository,
	slack *slack.Slack,
	vertex *ai.Vertex,
) (*Service, error) {
	s := &Service{cfg: cfg,
		gemini:     gemini,
		repository: repository,
		slack:      slack,
		vertex:     vertex}
	return s, nil
}
