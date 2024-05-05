# Build Stage
FROM openjdk:11 AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Production Stage
FROM openjdk:11

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=9090", "app.jar"]
