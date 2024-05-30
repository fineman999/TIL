package service

import (
	"context"
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/types"
	"github.com/google/generative-ai-go/genai"
	"log"
	"mime/multipart"
	"path/filepath"
	"strings"
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

func (s *Service) ImageTest(ctx context.Context, files []*multipart.FileHeader, req types.ChatTestGeminiRequest) (*genai.GenerateContentResponse, error) {
	imageFiles := make([]*types.ImageDto, 0)
	for _, file := range files {
		log.Println(file.Filename)
		fileExt := filepath.Ext(file.Filename)
		originalFileName := strings.TrimSuffix(filepath.Base(file.Filename), filepath.Ext(file.Filename))
		imageFiles = append(imageFiles, &types.ImageDto{
			Image: file,
			Ext:   fileExt,
			Name:  originalFileName,
		})
	}
	imageTestGeminiDto := &types.ImageTestGeminiDto{
		Images: imageFiles,
		Text:   req.Text,
	}

	response, err := s.gemini.ImageTest(ctx, imageTestGeminiDto)
	if err != nil {
		return nil, err
	}
	return response, nil
}
