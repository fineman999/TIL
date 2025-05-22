package service

import (
	"context"
	"fmt"
	"practice/creational-patterns/factory/internal/domain"
	"sync"
)

// MessageService는 메시지 전송 유스케이스를 처리
type MessageService struct {
	factory   domain.MessagingFactory
	senders   map[domain.SenderType]domain.MessageSender
	formatter domain.MessageFormatter
	mu        sync.RWMutex // 동시성 제어
	once      sync.Once    // 포맷터 지연 초기화
}

// NewMessageService는 새로운 MessageService를 생성
func NewMessageService(factory domain.MessagingFactory) *MessageService {
	return &MessageService{
		factory: factory,
		senders: make(map[domain.SenderType]domain.MessageSender),
	}
}
func (s *MessageService) getFormatter() domain.MessageFormatter {
	s.once.Do(func() {
		s.formatter = s.factory.CreateFormatter()
	})
	return s.formatter
}

func (s *MessageService) getSender(senderType domain.SenderType) domain.MessageSender {
	s.mu.RLock()
	sender, exists := s.senders[senderType]
	s.mu.RUnlock()
	if exists {
		return sender
	}

	s.mu.Lock()
	defer s.mu.Unlock()
	// 재확인 (Double-checked locking)
	if sender, exists = s.senders[senderType]; !exists {
		sender = s.factory.CreateSender(senderType)
		if sender != nil {
			s.senders[senderType] = sender
		}
	}
	return sender
}

// SendMessage는 지정된 senderType으로 메시지를 전송
func (s *MessageService) SendMessage(ctx context.Context, senderType domain.SenderType, content string) string {
	if !senderType.IsValid() {
		return fmt.Sprintf("Unknown sender type: %s", senderType.String())
	}
	sender := s.getSender(senderType)
	if sender == nil {
		return fmt.Sprintf("Sender not supported for type: %s", senderType.String())
	}
	formatter := s.getFormatter()
	formattedContent := formatter.Format(content)
	err := sender.SendMessage(ctx, formattedContent)
	if err != nil {
		return fmt.Sprintf("Failed to send message: %s", err.Error())
	}
	return ""
}
