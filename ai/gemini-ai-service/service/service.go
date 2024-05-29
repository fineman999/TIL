package service

import (
	"context"
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/types"
)

type Service struct {
	cfg    *config.Config
	gemini *ai.Gemini
}

func NewService(cfg *config.Config, gemini *ai.Gemini) (*Service, error) {
	s := &Service{cfg: cfg, gemini: gemini}
	return s, nil
}

func (s *Service) TestGemini(ctx context.Context, prompt string) (*types.TestGeminiResponse, error) {
	text, err := s.gemini.GenerateText(ctx, prompt)
	if err != nil {
		return nil, err
	}

	return &types.TestGeminiResponse{
		Payload: text,
	}, nil

}
