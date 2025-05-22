package main

import (
	"context"
	"fmt"
	"github.com/joho/godotenv"
	"os"
	"practice/creational-patterns/factory/config"
	"practice/creational-patterns/factory/internal/application/service"
	"practice/creational-patterns/factory/internal/domain"
	"practice/creational-patterns/factory/internal/domain/model"
	"practice/creational-patterns/factory/internal/factory"
)

const (
	EnvDev  = "dev"
	EnvProd = "prod"
)

func main() {
	if err := godotenv.Load(); err != nil {
		fmt.Println("Error loading .env file, falling back to system environment variables")
	}

	env := os.Getenv("ENV")
	newConfig := config.NewConfig()
	var messagingFactory domain.MessagingFactory
	switch env {
	case EnvProd:
		messagingFactory = factory.NewProductionFactory(newConfig)
	case EnvDev:
		messagingFactory = factory.NewDevelopmentFactory(newConfig)
	default:
		panic(fmt.Sprintf("Unknown environment: %s", env))
	}
	// 메시지 서비스 초기화
	messageService := service.NewMessageService(messagingFactory)
	// 테스트 메시지 전송
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	fmt.Println(messageService.SendMessage(ctx, domain.SenderTypeSlack, "Hello, this is a Slack message!"))
	fmt.Println(messageService.SendMessage(ctx, domain.SenderTypeDiscord, "Hello, this is a Discord message!"))
	fmt.Println(messageService.SendMessage(ctx, domain.SenderTypeUnknown, "This should fail"))

	// 빌더 패턴 활용
	message := model.NewMessageBuilder().
		WithContent("Hello, this is a message!").
		WithSender("유저").Build()

	fmt.Println(messageService.SendMessageWithSender(ctx, domain.SenderTypeSlack, message))
	fmt.Println(messageService.SendMessageWithSender(ctx, domain.SenderTypeDiscord, message))
	fmt.Println(messageService.SendMessageWithSender(ctx, domain.SenderTypeUnknown, message))

}
