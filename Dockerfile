FROM maven:3.9.8-eclipse-temurin-21 AS build

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

COPY --from=build /target/Otrio-0.0.1-SNAPSHOT.jar Otrio.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]