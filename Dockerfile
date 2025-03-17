FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Rename POM.xml to pom.xml (case-sensitive in Linux)

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]