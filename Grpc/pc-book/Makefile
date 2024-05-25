gen:
	protoc --go_out=./pb --go_opt=paths=source_relative --go-grpc_out=./pb --go-grpc_opt=paths=source_relative proto/*.proto
clean:
	rm -rf pb/*
server:
	go run cmd/server/main.go -port 8081
client:
	go run cmd/client/main.go -address 0.0.0.0:8081
test:
	go test -cover -race ./...