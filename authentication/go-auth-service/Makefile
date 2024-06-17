DOCKER_IMAGE = "go-auth-service"

build:
	sudo docker build -t $(DOCKER_IMAGE) .
run-local:
	sudo docker run -p 8080:8080 $(DOCKER_IMAGE)
run-dev:
	sudo docker run -p 80:8080 $(DOCKER_IMAGE)
run-prod:
	sudo docker run -p 80:80 $(DOCKER_IMAGE)