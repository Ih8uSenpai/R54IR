FROM openjdk:17
FROM maven:3.8.1-openjdk-17

WORKDIR /app

COPY . .

RUN mvn clean package

EXPOSE 8081

CMD ["java", "-jar", "/app/target/security-0.0.1-SNAPSHOT.jar"]

