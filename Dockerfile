# syntax=docker/dockerfile:1

FROM gradle:8.7-jdk17 AS build
WORKDIR /workspace
COPY build.gradle settings.gradle* gradle.properties* ./
COPY src ./src
RUN gradle --no-daemon clean bootJar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
