package service

import (
	"context"
	"fmt"
	"practice/creational-patterns/factory/internal/domain"
	"practice/creational-patterns/factory/internal/infrastructure/discord"
	"practice/creational-patterns/factory/internal/infrastructure/slack"
)

// MessageService는 메시지 전송 유스케이스를 처리
type MessageService struct {
	senders map[domain.SenderType]domain.MessageSender
}

// NewMessageService는 새로운 MessageService를 생성
func NewMessageService(senders ...domain.MessageSender) *MessageService {
	senderMap := make(map[domain.SenderType]domain.MessageSender)
	for _, sender := range senders {
		switch sender.(type) {
		case *slack.Sender:
			senderMap[domain.SenderTypeSlack] = sender
		case *discord.Sender:
			senderMap[domain.SenderTypeDiscord] = sender
		}
	}
	return &MessageService{senders: senderMap}
}

// SendMessage는 지정된 senderType으로 메시지를 전송
func (s *MessageService) SendMessage(ctx context.Context, senderType domain.SenderType, content string) string {
	if !senderType.IsValid() {
		return fmt.Sprintf("Unknown sender type: %s", senderType.String())
	}
	sender, exists := s.senders[senderType]
	if !exists {
		return fmt.Sprintf("Sender not registered for type: %s", senderType.String())
	}
	err := sender.SendMessage(ctx, content)
	if err != nil {
		return fmt.Sprintf("Failed to send message: %s", err.Error())
	}
	return ""
}
