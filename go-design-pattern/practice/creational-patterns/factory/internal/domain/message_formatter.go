package domain

type MessageFormatter interface {
	Format(content string) string
}
