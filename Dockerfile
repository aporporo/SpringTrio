FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
# Copy pom.xml first
COPY POM.xml .
# Copy source files
COPY src/ ./src/
# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/Otrio-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]