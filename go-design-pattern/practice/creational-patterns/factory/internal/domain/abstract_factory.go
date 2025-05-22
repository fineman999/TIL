package domain

type MessagingFactory interface {
	CreateSender(senderType SenderType) MessageSender
	CreateFormatter() MessageFormatter
}
