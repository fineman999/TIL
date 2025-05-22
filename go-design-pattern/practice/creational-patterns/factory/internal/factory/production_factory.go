package factory

import (
	"practice/creational-patterns/factory/config"
	"practice/creational-patterns/factory/internal/domain"
	"practice/creational-patterns/factory/internal/infrastructure/discord"
	"practice/creational-patterns/factory/internal/infrastructure/formatter"
	"practice/creational-patterns/factory/internal/infrastructure/slack"
)

type productionFactory struct {
	cfg *config.Config
}

func (f *productionFactory) CreateSender(senderType domain.SenderType) domain.MessageSender {
	switch senderType {
	case domain.SenderTypeSlack:
		return slack.NewRealSender(f.cfg)
	case domain.SenderTypeDiscord:
		return discord.NewRealSender(f.cfg)
	default:
		return nil
	}
}

func (f *productionFactory) CreateFormatter() domain.MessageFormatter {
	return formatter.NewStandardFormatter()
}

func NewProductionFactory(
	cfg *config.Config,
) domain.MessagingFactory {
	return &productionFactory{
		cfg: cfg,
	}
}
