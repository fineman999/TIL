package main

import (
	"fmt"
	"practice/creational-patterns/prototype/domain"
)

func main() {
	// 초기 Document 객체 생성
	original := domain.NewDocument("Report", "This is a detailed report.")
	original.AddMetadata("Author", "Bob")
	original.AddMetadata("Version", "1.0")

	// 프로토타입 패턴으로 복제
	cloned := original.Clone()
	cloned.Title = "Cloned Report" // 복제된 객체 수정
	cloned.AddMetadata("Version", "2.0")

	fmt.Printf("Original: Title=%s, Metadata=%v\n", original.Title, original.Metadata)
	fmt.Printf("Cloned: Title=%s, Metadata=%v\n", cloned.Title, cloned.Metadata)
}
