package discord

import (
	"context"
	"fmt"
	"practice/creational-patterns/factory/internal/domain"
)

type MockSender struct {
}

func (s *MockSender) SendMessage(ctx context.Context, content string) error {
	sprintf := fmt.Sprintf("Mock Discord message sent: %s", content)
	fmt.Println(sprintf)
	return nil
}

func NewMockSender() domain.MessageSender {
	return &MockSender{}
}
