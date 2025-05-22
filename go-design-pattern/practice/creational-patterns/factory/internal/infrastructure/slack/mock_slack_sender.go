package slack

import (
	"context"
	"fmt"
	"practice/creational-patterns/factory/config"
	"practice/creational-patterns/factory/internal/domain"
)

type MockSender struct {
}

func (s *MockSender) SendMessage(ctx context.Context, content string) error {
	sprintf := fmt.Sprintf("Mock Slack message sent: %s", content)
	fmt.Println(sprintf)
	return nil
}

func NewMockSender(cfg *config.Config) domain.MessageSender {
	return &MockSender{}
}
