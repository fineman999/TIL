package service

import (
	"context"
	"gemini-ai-service/types"
	"mime/multipart"
)

func (s *Service) UploadFile(ctx context.Context, file multipart.File, header *multipart.FileHeader) (*types.UploadFileRes, error) {
	filename := header.Filename
	uploadFileName, err := s.storage.UploadFile(ctx, filename, file)
	if err != nil {
		return nil, err
	}

	return &types.UploadFileRes{
		Url: uploadFileName,
	}, nil
}
