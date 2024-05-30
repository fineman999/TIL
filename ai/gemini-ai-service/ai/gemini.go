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

	//var maxOutputTokens int32 = 100

	/**
	자세한 매개변수 요약표는 https://wikidocs.net/229820 에서...
	temperature=0이나 top_p=0으로 설정했다고 해서 매번 완전히 동일한 결과만 생성하는 것은 아닙니다.
	거대언어모델은 클라우드 환경에서 여러 노드의 리소스를 병렬적으로 사용하는 방식으로 수많은 연산을 수행할 뿐만 아니라,
	확률분포에 동일한 확률값을 갖는 단어들도 나타날 수 있으므로 항상 같은 결과를 보장하기는 어렵습니다.
	*/
	/**
	temperature를 높게 설정하면 모델이 생성하는 언어의 예측 가능성은 떨어지고 그만큼 독창성은 올라갑니다.
	반대로 temperature가 낮아지면 안정적이면서도 일관된 답변을 생성합니다.
	이런 이유로 temperature를 1로 설정하면 아무런 변화를 주지 않지만,
	1보다 작은 값을 주면 뾰족한 확률분포를,
	1보다 큰 값을 주면 펑퍼짐한 확률분포를 만들게 됩니다.
	*/
	//var temperature float32 = 0.5 // 확률분포를 조정하는 매개변수
	//var topP float32 = 0.9 // top_p는 확률분포 내에서 선택할 단어의 범위를 결정하는 매개변수
	//generationConfig := genai.GenerationConfig{
	//StopSequences: []string{".", "!", "?"}, // 해당 문자열을 만나면 생성을 중단합니다. 최대 5개까지 설정 가능
	//MaxOutputTokens: &maxOutputTokens, // 생성할 토큰의 최대 개수를 설정합니다. 최대 2048까지 설정 가능
	//Temperature: &temperature, // 생성할 텍스트의 다양성을 조절합니다. 0.0에서 2.0 사이의 값을 설정합니다. 기본값은 1입니다.
	//TopP: 0.9, // Top-p sampling을 사용하여 생성할 텍스트의 다양성을 조절합니다. 0.0에서 1.0 사이의 값을 설정합니다. 기본값은 1입니다.
	//}
	// The Gemini 1.5 models are versatile and work with most use cases
	model := client.GenerativeModel("gemini-1.5-flash")
	// 설정하기
	//model.GenerationConfig.StopSequences = generationConfig.StopSequences
	//model.GenerationConfig.MaxOutputTokens = generationConfig.MaxOutputTokens
	//model.GenerationConfig.Temperature = generationConfig.Temperature
	//model.GenerationConfig.TopP = generationConfig.TopP

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
	part := resp.Candidates[0].Content.Parts[0]
	tokens, err := g.geminiModel.CountTokens(ctx, part)
	if err != nil {
		return nil, err
	}
	log.Println("Token의 개수: ", tokens.TotalTokens)
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
