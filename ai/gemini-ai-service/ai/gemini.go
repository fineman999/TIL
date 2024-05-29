package ai

import (
	"context"
	"gemini-ai-service/config"
	"github.com/google/generative-ai-go/genai"
	"google.golang.org/api/option"
	"log"
)

type Gemini struct {
	client      *genai.Client
	geminiModel *genai.GenerativeModel
}

func NewGemini(cfg *config.Config) (*Gemini, error) {
	ctx := context.Background()
	// Access your API key as an environment variable (see "Set up your API key" above)
	client, err := genai.NewClient(ctx, option.WithAPIKey(cfg.Gemini.Key))
	if err != nil {
		return nil, err
	}

	// The Gemini 1.5 models are versatile and work with most use cases
	model := client.GenerativeModel("gemini-1.5-flash")
	return &Gemini{
		geminiModel: model,
		client:      client,
	}, nil
}

func (g *Gemini) GenerateText(ctx context.Context, prompt string) (*genai.GenerateContentResponse, error) {
	var err error
	resp, err := g.geminiModel.GenerateContent(ctx, genai.Text(prompt))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

func (g *Gemini) Close() error {
	log.Println("----- Closing Gemini client ----")
	return g.client.Close()
}
