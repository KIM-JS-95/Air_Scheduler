#!/bin/bash

# 현재 디렉토리 위치 저장
current_dir=$(pwd)
username="baugh248730"

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
./gradlew build -x test

# Docker 이미지 빌드
echo "Building Docker images..."
docker-compose build

# Docker Hub 로그인
echo "Docker Hub에 로그인하세요."
docker login

# 이미지 태그 지정
docker tag table-detection $username/flight_schedules:latest
echo "Tagged Docker image"

# 이미지 푸시
docker push $username/table-detection:latest
echo "Pushed Docker image"

# Docker 컨테이너 실행
echo "Starting services with Docker Compose..."
docker-compose up -d

echo "Docker push complete."
