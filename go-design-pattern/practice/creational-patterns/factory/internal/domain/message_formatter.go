package domain

type MessageFormatter interface {
	Format(content string) string
	FormatWithSender(sender string, content string) string
}
