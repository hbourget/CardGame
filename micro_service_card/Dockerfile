FROM maven:3-eclipse-temurin-17 as build
WORKDIR /app
COPY . .
RUN mvn install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/service-card-0.0.1-SNAPSHOT.jar .
EXPOSE 8082
CMD ["java", "-jar", "service-card-0.0.1-SNAPSHOT.jar"]