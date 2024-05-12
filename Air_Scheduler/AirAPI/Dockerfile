# Build Stage
FROM openjdk:11-slim as builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Production Stage
FROM openjdk:11-slim
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dserver.port=9090", "app.jar"]
