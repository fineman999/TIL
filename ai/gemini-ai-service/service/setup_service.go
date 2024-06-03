package service

import (
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/gcs"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/slack"
)

type Service struct {
	cfg        *config.Config
	gemini     *ai.Gemini
	vertex     *ai.Vertex
	repository *infrastructure.Repository
	slack      *slack.Slack
	storage    *gcs.Storage
}

func NewService(cfg *config.Config, gemini *ai.Gemini, repository *infrastructure.Repository, slack *slack.Slack, vertex *ai.Vertex, storage *gcs.Storage) (*Service, error) {
	s := &Service{cfg: cfg,
		gemini:     gemini,
		repository: repository,
		slack:      slack,
		vertex:     vertex,
		storage:    storage}
	return s, nil
}
