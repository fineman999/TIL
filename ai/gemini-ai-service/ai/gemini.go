package ai

import (
	"context"
	"errors"
	"gemini-ai-service/config"
	"gemini-ai-service/infrastructure"
	"gemini-ai-service/types"
	"github.com/google/generative-ai-go/genai"
	"google.golang.org/api/option"
	"io"
	"log"
)

type Gemini struct {
	client      *genai.Client
	geminiModel *genai.GenerativeModel
	chats       map[int]*genai.ChatSession
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
		chats:       make(map[int]*genai.ChatSession),
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

func (g *Gemini) StartChatWithRooms(id int, rooms []infrastructure.Room) {
	cs := g.geminiModel.StartChat()
	var contents []*genai.Content
	for _, room := range rooms {
		if room.Type == infrastructure.USER {
			contents = append(contents, &genai.Content{
				Parts: []genai.Part{
					genai.Text(room.Text),
				},
				Role: "user",
			})
		} else {
			contents = append(contents, &genai.Content{
				Parts: []genai.Part{
					genai.Text(room.Text),
				},
				Role: "model",
			})
		}
	}
	cs.History = contents
	g.chats[id] = cs
}

func (g *Gemini) GenerateChatText(ctx context.Context, room infrastructure.Room, id int) (*genai.GenerateContentResponse, error) {
	cs := g.chats[id]
	if cs == nil {
		return nil, errors.New("chat session is nil")
	}
	resp, err := cs.SendMessage(ctx, genai.Text(room.Text))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

func (g *Gemini) StartChat(id int) {
	cs := g.geminiModel.StartChat()
	g.chats[id] = cs
}

func (g *Gemini) ImageTest(ctx context.Context, parts *types.ImageTestGeminiDto) (*genai.GenerateContentResponse, error) {

	prompt := make([]genai.Part, 0)

	for _, fileTemp := range parts.Images {
		file, err := fileTemp.Image.Open()
		if err != nil {
			log.Println(err)
			return nil, err
		}
		defer file.Close()

		bytes, err := io.ReadAll(file)
		if err != nil {
			log.Println(err)
			return nil, err
		}
		prompt = append(prompt, genai.ImageData(fileTemp.Ext, bytes))
	}

	prompt = append(prompt, genai.Text(parts.Text))
	resp, err := g.geminiModel.GenerateContent(ctx, prompt...)
	if err != nil {
		log.Fatal(err)
		return nil, err
	}

	return resp, nil
}
