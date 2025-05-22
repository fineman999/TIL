package model

// 메시지 값 객체
type Message struct {
	Sender  string
	Content string
}

// MessageBuilder는 Message 객체를 생성하기 위한 빌더
type MessageBuilder struct {
	sender  string
	content string
}

// NewMessageBuilder는 새로운 MessageBuilder를 생성
func NewMessageBuilder() *MessageBuilder {
	return &MessageBuilder{}
}

func (b *MessageBuilder) WithSender(sender string) *MessageBuilder {
	b.sender = sender
	return b
}

func (b *MessageBuilder) WithContent(content string) *MessageBuilder {
	b.content = content
	return b
}

// Build는 최종 Message 객체를 생성
func (b *MessageBuilder) Build() *Message {
	return &Message{
		Sender:  b.sender,
		Content: b.content,
	}
}
