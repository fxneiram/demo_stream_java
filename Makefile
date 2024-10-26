# Variables
APP_NAME = demo-stram-java
JAR_FILE = target/$(APP_NAME).jar
DOCKER_IMAGE = springboot-stream-app:latest

# Meta targets
.PHONY: all clean build docker-build docker-run docker-compose-up

# Default target: build all
all: clean build docker-build docker-compose-up

clean:
	@echo "Cleaning project..."
	mvn clean

build:
	@echo "Building JAR..."
	mvn clean package

docker-build: $(JAR_FILE)
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE) .

docker-run:
	@echo "Running Docker image..."
	docker run -p 8082:8082 $(DOCKER_IMAGE)

docker-compose-up:
	@echo "Up the services with Docker Compose..."
	docker-compose up --build

docker-compose-down:
	@echo "Down the services with Docker Compose..."
	docker-compose down

file-upload:
	@echo "Uploading file..."
	curl -X POST http://localhost:8082/api/files/upload -F file=@"C:\Users\fasan\OneDrive\Desktop\Libros TTS\A1\Caperucita Roja\input.mp4"

