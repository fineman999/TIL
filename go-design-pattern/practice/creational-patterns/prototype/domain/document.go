package domain

import "time"

// Document는 복잡한 문서 객체를 나타냄
type Document struct {
	Title     string
	Content   string
	CreatedAt time.Time
	Metadata  map[string]string
}

// NewDocument는 초기 Document 객체를 생성
func NewDocument(title, content string) *Document {
	return &Document{
		Title:     title,
		Content:   content,
		CreatedAt: time.Now(),
		Metadata:  make(map[string]string),
	}
}

// Clone은 Document 객체를 복제
func (d *Document) Clone() *Document {
	// 깊은 복사를 위해 Metadata 맵을 새로 생성
	newMetadata := make(map[string]string)
	for k, v := range d.Metadata {
		newMetadata[k] = v
	}

	return &Document{
		Title:     d.Title,
		Content:   d.Content,
		CreatedAt: d.CreatedAt, // time.Time은 immutable이므로 복사 안전
		Metadata:  newMetadata,
	}
}

// AddMetadata는 메타데이터를 추가
func (d *Document) AddMetadata(key, value string) {
	d.Metadata[key] = value
}
