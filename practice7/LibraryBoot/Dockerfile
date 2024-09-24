FROM openjdk:17
FROM maven:3.8.1-openjdk-17

WORKDIR /app

COPY . .

RUN mvn clean package

EXPOSE 8080

CMD ["java", "-jar", "/app/target/LibraryBoot-0.0.1-SNAPSHOT.jar"]

