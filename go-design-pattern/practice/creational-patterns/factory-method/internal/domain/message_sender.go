package domain

import "context"

type MessageSender interface {
	SendMessage(ctx context.Context, content string) error
}
