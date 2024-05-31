package slack

import (
	"context"
	"fmt"
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

func (s *Slack) SendMessage(ctx context.Context, message string) error {
	_, _, _, err := s.client.SendMessageContext(
		ctx,
		s.channel,
		slack.MsgOptionText(message, false),
	)
	return err
}

func (s *Slack) SendSlashCommand(ctx context.Context, command slack.SlashCommand) error {

	text := fmt.Sprintf(`
안녕하세요! <@%s>님, 
지금은 테스트 중입니다.
다음과 같이 보내셨군요? 
%s
`, command.UserID, command.Text)
	return s.SendMessage(ctx, text)

}

func (s *Slack) SendSlashCommandWithAI(ctx context.Context, response string, slackCommand slack.SlashCommand) error {
	return s.SendMessageWithChannelID(ctx, response, slackCommand)
}

func (s *Slack) SendMessageWithChannelID(ctx context.Context, response string, slackCommand slack.SlashCommand) error {

	text := fmt.Sprintf(`
<@%s>님: %s
Gemini: %s
`, slackCommand.UserID, slackCommand.Text, response)

	_, _, _, err := s.client.SendMessageContext(
		ctx,
		slackCommand.ChannelID,
		slack.MsgOptionText(text, false),
	)
	return err
}
