package service

import (
	"cloud.google.com/go/vertexai/genai"
	"context"
	"gemini-ai-service/types"
)

func (s *Service) GenerateTextWithVertex(ctx context.Context, text string) (string, error) {
	text, err := s.vertex.GenerateResponse(ctx, text)
	if err != nil {
		return "", err
	}

	return text, nil
}

func (s *Service) GenerateVideoUrl(ctx context.Context, request types.VideoUrlRequest) (*genai.GenerateContentResponse, error) {
	resp, err := s.vertex.GenerateVideoUrl(ctx, request)
	if err != nil {
		return nil, err
	}

	return resp, nil
}
