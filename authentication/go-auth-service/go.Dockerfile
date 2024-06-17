# 빌드 스테이지
FROM golang:1.22.3-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# go.mod와 go.sum 파일을 컨테이너에 복사
COPY go.mod go.sum ./

# 의존성 다운로드
RUN go mod download

# 소스 코드를 컨테이너에 복사
COPY . .

# Go 어플리케이션 빌드
RUN go build -o main

# 실행 스테이지
FROM alpine:latest

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 빌드한 실행 파일을 현재 스테이지에 복사
COPY --from=builder /app/main .

# config.toml 파일을 현재 스테이지에 복사
COPY --from=builder /app/config.toml ./config.toml
#
# 서비스할 포트 설정
EXPOSE 8080

# 컨테이너 실행 시 실행할 명령어 설정
CMD ["./main"]