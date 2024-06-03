package ai

import (
	"cloud.google.com/go/vertexai/genai"
	"context"
	"gemini-ai-service/config"
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
