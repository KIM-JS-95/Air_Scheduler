#!/bin/bash



# 현재 디렉토리 위치 저장
current_dir=$(pwd)

# Dockerfile, docker-compose.yml 파일이 있는지 확인
if [ ! -f "$current_dir/Dockerfile" ] || [ ! -f "$current_dir/docker-compose.yml" ]; then
    echo "Error: Dockerfile or docker-compose.yml not found in the current directory."
    exit 1
fi

# 컨테이너 중지 및 제거
echo "Stopping and removing existing containers..."
docker stop mysql
docker rm mysql

# Gradle Wrapper 실행
echo "Building Spring Boot application with Gradle Wrapper..."
./gradlew build

# Docker 이미지 빌드
echo "Building Docker images..."
docker-compose build

# Docker 컨테이너 실행
echo "Starting services with Docker Compose..."
docker-compose up -d

echo "Deployment complete."
