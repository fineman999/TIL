package types

import "github.com/google/generative-ai-go/genai"

type TestGeminiRequest struct {
	Text string `json:"text" binding:"required"`
}
type TestGeminiResponse struct {
	Payload *genai.GenerateContentResponse
}
