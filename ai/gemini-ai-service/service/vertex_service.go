package service

import "context"

func (s *Service) GenerateTextWithVertex(ctx context.Context, text string) (string, error) {
	text, err := s.vertex.GenerateResponse(ctx, text)
	if err != nil {
		return "", err
	}

	return text, nil
}
