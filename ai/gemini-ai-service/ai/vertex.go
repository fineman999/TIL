package ai

import (
	"cloud.google.com/go/vertexai/genai"
	"context"
	"errors"
	"fmt"
	"gemini-ai-service/config"
	"gemini-ai-service/types"
	"log"
	"strings"
)

type Vertex struct {
	model  *genai.GenerativeModel
	client *genai.Client
}

/*
gemini-1.5-pro: (미리보기) 멀티모달(텍스트, 이미지, 오디오, PDF, 코드, 동영상)로 생성되며, 최대 100만 개 입력 토큰까지 다양한 태스크 범위로 확장됩니다.
gemini-1.0-pro: 자연어 태스크, 멀티턴 텍스트 및 코드 채팅, 코드 생성을 처리하도록 설계되었습니다.
gemini-1.0-pro-vision: 멀티모달 프롬프트를 지원합니다. 프롬프트 요청에 텍스트, 이미지, 동영상을 포함하고 텍스트 또는 코드 응답을 얻을 수 있습니다.
*/
func NewVertex(cfg *config.Config) (*Vertex, error) {
	ctx := context.Background()
	client, err := genai.NewClient(ctx, cfg.Vertex.Project, cfg.Vertex.Location)
	if err != nil {
		return nil, err
	}

	model := client.GenerativeModel("gemini-1.0-pro-vision")

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

func (v *Vertex) GenerateVideoUrl(ctx context.Context, request types.VideoUrlRequest) (*genai.GenerateContentResponse, error) {
	data := genai.FileData{
		MIMEType: request.MIMEType,
		FileURI:  request.Url,
	}
	prompt := genai.Text(request.Text)
	resp, err := v.model.GenerateContent(ctx, data, prompt)
	if err != nil {
		var blockedErr *genai.BlockedError
		if errors.As(err, &blockedErr) {
			if blockedErr != nil && blockedErr.PromptFeedback != nil {
				reason := blockedErr.PromptFeedback.BlockReason
				log.Println("다음의 문제가 발생하여 응답이 중단되었습니다.: ", reason)
				return nil, blockedErr
			}
			if blockedErr != nil && blockedErr.Candidate != nil {
				reason := blockedErr.Candidate.FinishReason
				log.Println("다음의 문제가 발생하여 응답이 중단되었습니다.: ", reason)
				return nil, blockedErr
			}
			return nil, blockedErr
		}
		return nil, err
	}
	return resp, nil
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
