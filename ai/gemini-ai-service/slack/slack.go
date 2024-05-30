package slack

import (
	"gemini-ai-service/config"
	"github.com/slack-go/slack"
)

type Slack struct {
	client  *slack.Client
	token   string
	channel string
}

func NewSlack(cfg *config.Config) (*Slack, error) {
	client := slack.New(cfg.Slack.Token)
	return &Slack{
		client:  client,
		token:   cfg.Slack.Token,
		channel: cfg.Slack.Channel,
	}, nil
}

func (s *Slack) SendMessage(message string) error {
	_, _, _, err := s.client.SendMessage(
		s.channel,
		slack.MsgOptionText(message, false),
	)
	return err
}
