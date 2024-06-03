package ai

import (
	"cloud.google.com/go/vertexai/genai"
	"context"
	"errors"
	"fmt"
	"gemini-ai-service/config"
	"log"
	"strings"
)

type Vertex struct {
	model  *genai.GenerativeModel
	client *genai.Client
}

func NewVertex(cfg *config.Config) (*Vertex, error) {
	ctx := context.Background()
	client, err := genai.NewClient(ctx, cfg.Vertex.Project, cfg.Vertex.Location)
	if err != nil {
		return nil, err
	}

	model := client.GenerativeModel("gemini-1.0-pro")

	return &Vertex{
		model:  model,
		client: client,
	}, nil
}

func (v *Vertex) GenerateResponse(ctx context.Context, prompt string) (string, error) {
	resp, err := v.model.GenerateContent(ctx, genai.Text(prompt))
	if err != nil {
		return "", err
	}
	if err != nil {
		var blockedErr *genai.BlockedError
		if errors.As(err, &blockedErr) {
			if blockedErr != nil && blockedErr.PromptFeedback != nil {
				reason := blockedErr.PromptFeedback.BlockReason
				log.Println("다음의 문제가 발생하여 응답이 중단되었습니다.: ", reason)
				return "", blockedErr
			}
			if blockedErr != nil && blockedErr.Candidate != nil {
				reason := blockedErr.Candidate.FinishReason
				log.Println("다음의 문제가 발생하여 응답이 중단되었습니다.: ", reason)
				return "", blockedErr
			}
			return "", blockedErr
		}
		return "", err
	}
	response := v.formatResponse(resp)
	return response, nil
}

func (v *Vertex) formatResponse(resp *genai.GenerateContentResponse) string {
	var formattedContent strings.Builder
	if resp != nil && resp.Candidates != nil {
		for _, cand := range resp.Candidates {
			if cand.Content != nil {
				for _, part := range cand.Content.Parts {
					formattedContent.WriteString(fmt.Sprintf("%v", part))
				}
			}
		}
	}

	return formattedContent.String()
}
