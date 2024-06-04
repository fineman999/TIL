package service

import (
	"cloud.google.com/go/vertexai/genai"
	"context"
	"gemini-ai-service/types"
	"log"
	"mime/multipart"
	"time"
)

func (s *Service) GenerateTextWithVertex(ctx context.Context, text string) (string, error) {
	text, err := s.vertex.GenerateResponse(ctx, text)
	if err != nil {
		return "", err
	}

	return text, nil
}

func (s *Service) GenerateVideoUrl(ctx context.Context, request types.VideoUrlRequest) (*genai.GenerateContentResponse, error) {
	resp, err := s.vertex.GenerateVideoUrl(ctx, &request)
	if err != nil {
		return nil, err
	}

	return resp, nil
}

func (s *Service) GenerateFile(ctx context.Context, file multipart.File, header *multipart.FileHeader, text string) (*genai.GenerateContentResponse, error) {
	filename := header.Filename
	fileDto, err := s.storage.UploadFile(ctx, filename, file)
	if err != nil {
		return nil, err
	}
	fileDto.Text = text

	generateFile, err := s.vertex.GenerateFile(ctx, fileDto)
	if err != nil {
		return nil, err
	}

	go func() {
		nCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
		defer cancel()
		err := s.storage.DeleteFile(nCtx, fileDto.FileName)
		if err != nil {
			log.Println(err)
		}
	}()

	return generateFile, nil
}
