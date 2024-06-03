package types

import (
	"github.com/google/generative-ai-go/genai"
	"mime/multipart"
)

type TestGeminiRequest struct {
	Text string `json:"text" binding:"required"`
}
type TestGeminiResponse struct {
	Payload *genai.GenerateContentResponse
}

type ChatTestGeminiRequest struct {
	Text string `json:"text" binding:"required"`
}

type ImageDto struct {
	Image *multipart.FileHeader
	Ext   string
	Name  string
}

type ImageTestGeminiDto struct {
	Images []*ImageDto `json:"image" binding:"required"`
	Text   string      `json:"text" binding:"required"`
	Ext    string      `json:"ext" binding:"required"`
}

type VideoUrlRequest struct {
	Url      string `json:"url" binding:"required"`
	Text     string `json:"text" binding:"required"`
	MIMEType string `json:"mimeType" binding:"required"`
}

type UploadFileRes struct {
	Url string `json:"url"`
}
