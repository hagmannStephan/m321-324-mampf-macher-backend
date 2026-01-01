# syntax=docker/dockerfile:1

ARG SERVICE_DIR
# Default if not set
ARG SERVICE_DIR=recipe-finder-microservice

FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy pom.xml and src from the specified service directory
COPY ${SERVICE_DIR}/pom.xml ./pom.xml
COPY ${SERVICE_DIR}/src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]