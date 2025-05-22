package domain

// SenderType은 메시지 전송 방식의 Enum
type SenderType int

const (
	SenderTypeUnknown SenderType = iota
	SenderTypeSlack
	SenderTypeDiscord
)

// String은 SenderType을 문자열로 변환
func (s SenderType) String() string {
	switch s {
	case SenderTypeSlack:
		return "slack"
	case SenderTypeDiscord:
		return "discord"
	default:
		return "unknown"
	}
}

// ParseSenderType은 문자열을 SenderType으로 변환
func ParseSenderType(s string) SenderType {
	switch s {
	case "slack":
		return SenderTypeSlack
	case "discord":
		return SenderTypeDiscord
	default:
		return SenderTypeUnknown
	}
}

// IsValid는 SenderType이 유효한지 확인
func (s SenderType) IsValid() bool {
	return s != SenderTypeUnknown
}
