package main

import (
	"context"
	"fmt"
	"practice/creational-patterns/factory-method/config"
	"practice/creational-patterns/factory-method/internal/application/service"
	"practice/creational-patterns/factory-method/internal/domain"
	"practice/creational-patterns/factory-method/internal/infrastructure/discord"
	"practice/creational-patterns/factory-method/internal/infrastructure/slack"
)

func main() {
	newConfig := config.NewConfig()
	// 메시지 서비스 초기화
	messageService := service.NewMessageService(
		slack.NewSlackSender(newConfig),
		discord.NewDiscordSender(newConfig),
	)

	// 테스트 메시지 전송
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	fmt.Println(messageService.SendMessage(ctx, domain.SenderTypeSlack, "Hello, this is a Slack message!"))
	fmt.Println(messageService.SendMessage(ctx, domain.SenderTypeDiscord, "Hello, this is a Discord message!"))
	fmt.Println(messageService.SendMessage(ctx, domain.SenderTypeUnknown, "This should fail"))
}
