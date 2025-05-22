package slack

import (
	"context"
	"github.com/slack-go/slack"
	"log"
	"practice/creational-patterns/factory/config"
	"practice/creational-patterns/factory/internal/domain"
)

type RealSender struct {
	token       string
	channel     string
	slackClient *slack.Client
}

func (s *RealSender) SendMessage(ctx context.Context, content string) error {
	messageContext, messageId, text, err := s.slackClient.SendMessageContext(
		ctx,
		s.channel,
		slack.MsgOptionText(content, false),
	)

	if err != nil {
		log.Printf("Slack Send Error: %v", err)
		return err
	}
	log.Printf("Slack Message Sent: %s", text)
	log.Printf("Slack Message ID: %s", messageId)
	log.Printf("Slack Message Context: %v", messageContext)
	return nil
}

func NewRealSender(cfg *config.Config) domain.MessageSender {
	return &RealSender{
		token:       cfg.Slack.Token,
		channel:     cfg.Slack.Channel,
		slackClient: slack.New(cfg.Slack.Token),
	}
}
