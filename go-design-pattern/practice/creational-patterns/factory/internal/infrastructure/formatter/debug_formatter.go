package formatter

import (
	"fmt"
	"practice/creational-patterns/factory/internal/domain"
)

type debugFormatter struct{}

func (f *debugFormatter) FormatWithSender(sender string, content string) string {
	return fmt.Sprintf("[DEBUG] %s: %s", sender, content)
}

func (f *debugFormatter) Format(content string) string {
	return fmt.Sprintf("[DEBUG] %s", content)
}

func NewDebugFormatter() domain.MessageFormatter {
	return &debugFormatter{}
}
