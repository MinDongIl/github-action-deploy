# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

COPY build/libs/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.datasource.url=jdbc:mysql://shop-db.c74828wmikhx.ap-northeast-2.rds.amazonaws.com:3306/next?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8", "--spring.datasource.username=admin", "--spring.datasource.password=17Rwi[Cu*G[9*lGuXoWP)MFdyyVA"]


