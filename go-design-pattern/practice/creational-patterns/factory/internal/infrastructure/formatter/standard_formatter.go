package formatter

import (
	"fmt"
	"practice/creational-patterns/factory/internal/domain"
)

type standardFormatter struct{}

func (f *standardFormatter) Format(content string) string {
	return fmt.Sprintf("[PROD] %s", content)
}

func NewStandardFormatter() domain.MessageFormatter {
	return &standardFormatter{}
}
