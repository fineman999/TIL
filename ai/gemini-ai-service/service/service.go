package service

import (
	"context"
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/types"
	"github.com/google/generative-ai-go/genai"
)

type Service struct {
	cfg        *config.Config
	gemini     *ai.Gemini
	repository *infrastructure.Repository
}

func NewService(cfg *config.Config, gemini *ai.Gemini, repository *infrastructure.Repository) (*Service, error) {
	s := &Service{cfg: cfg, gemini: gemini, repository: repository}
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

func (s *Service) TestChat(ctx context.Context, request *types.ChatTestGeminiRequest, id int) (*genai.GenerateContentResponse, error) {
	room := infrastructure.Room{
		Text: request.Text,
		Type: infrastructure.USER,
	}
	s.repository.AddRoom(id, room)

	response, err := s.gemini.GenerateChatText(ctx, room, id)
	if err != nil {
		return nil, err
	}

	return response, nil
}

func (s *Service) CreateChat(id int) {

	rooms, err := s.repository.GetRoomByID(id)
	if err != nil {
		s.gemini.StartChat(id)
	} else {
		s.gemini.StartChatWithRooms(id, rooms)
	}
}
