FROM maven:3-eclipse-temurin-17-alpine as build
WORKDIR /app
COPY . .
RUN mvn install -DskipTests