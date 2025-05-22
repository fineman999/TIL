package factory

import (
	"practice/creational-patterns/factory/config"
	"practice/creational-patterns/factory/internal/domain"
	"practice/creational-patterns/factory/internal/infrastructure/discord"
	"practice/creational-patterns/factory/internal/infrastructure/formatter"
	"practice/creational-patterns/factory/internal/infrastructure/slack"
)

type developmentFactory struct {
	cfg *config.Config
}

func (f *developmentFactory) CreateSender(senderType domain.SenderType) domain.MessageSender {
	switch senderType {
	case domain.SenderTypeSlack:
		return slack.NewMockSender(f.cfg)
	case domain.SenderTypeDiscord:
		return discord.NewMockSender()
	default:
		return nil
	}
}

func (f *developmentFactory) CreateFormatter() domain.MessageFormatter {
	return formatter.NewDebugFormatter()
}

func NewDevelopmentFactory(
	cfg *config.Config,
) domain.MessagingFactory {
	return &developmentFactory{
		cfg: cfg,
	}
}
