package service

import (
	"context"
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/slack"
	"gemini-ai-service/types"
	"github.com/google/generative-ai-go/genai"
	slack2 "github.com/slack-go/slack"
	"log"
	"mime/multipart"
	"path/filepath"
	"strings"
)

type Service struct {
	cfg        *config.Config
	gemini     *ai.Gemini
	repository *infrastructure.Repository
	slack      *slack.Slack
}

func NewService(cfg *config.Config, gemini *ai.Gemini, repository *infrastructure.Repository, slack *slack.Slack) (*Service, error) {
	s := &Service{cfg: cfg, gemini: gemini, repository: repository, slack: slack}
	return s, nil
}

func (s *Service) TestGemini(ctx context.Context, prompt string) (*types.TestGeminiResponse, error) {
	text, err := s.gemini.GenerateResponse(ctx, prompt)
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

func (s *Service) TestSlack(ctx context.Context, text slack2.SlashCommand) error {
	err := s.slack.SendSlashCommand(ctx, text)
	if err != nil {
		return err
	}
	return nil
}

func (s *Service) SendSlackWithAI(ctx context.Context, parse slack2.SlashCommand) (string, error) {

	err2 := s.slack.SendMessageStartChat(ctx, parse)
	if err2 != nil {
		return "", err2
	}

	textResponse, err := s.gemini.GenerateText(ctx, parse.Text, parse)
	if err != nil {
		return "", err
	}

	return textResponse, nil
}
