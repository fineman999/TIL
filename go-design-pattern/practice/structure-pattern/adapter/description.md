# 1. 어댑터 패턴

### `MessageSender 인터페이스`
```go
package domain

import "context"

type MessageSender interface {
	SendMessage(ctx context.Context, content string) error
}

```

### `realSlackSender 구현체`
```go
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
```


## SlackSender와 어댑터 패턴:
- SlackSender는 MessageSender 인터페이스를 구현하며, slack.Client의 PostMessage 메서드를 호출합니다.
- 여기서 slack.Client는 Adaptee (외부 시스템의 인터페이스)로 볼 수 있고, SlackSender는 MessageSender 인터페이스(Target)를 구현하여 slack.Client를 클라이언트가 기대하는 형태로 변환합니다.
- 따라서 SlackSender는 어댑터 패턴을 적용한 것으로 볼 수 있습니다. slack.Client의 복잡한 API(PostMessage와 옵션)를 단순화하여 MessageSender 인터페이스의 SendMessage로 통합적으로 제공합니다.