# 빌드 스테이지
FROM golang:1.22.3-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# go.mod와 go.sum 파일을 컨테이너에 복사
COPY go.mod go.sum ./

# 의존성 다운로드
RUN go mod download
