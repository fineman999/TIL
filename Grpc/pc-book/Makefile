gen:
	protoc --go_out=./pb --go_opt=paths=source_relative --go-grpc_out=./pb --go-grpc_opt=paths=source_relative proto/*.proto
clean:
	rm -rf pb/*
server1:
	go run cmd/server/main.go -port 50051
server2:
	go run cmd/server/main.go -port 50052
server:
	go run cmd/server/main.go -port 8081
client:
	go run cmd/client/main.go -address 0.0.0.0:8081
test:
	go test -cover -race ./...

cert:
	cd cert && sudo sh ./gen.sh && cd ..

# 실제 파일이 아니라는 것을 make에게 알려주는 역할
.PHONY: gen clean server client test cert