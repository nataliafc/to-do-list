FROM ubuntu:latest AS build

RUN apt-get update && apt-get -y install openjdk-17-jdk

COPY . .

RUN apt-get -y install maven && mvn clean install

FROM openjdk:17-jdk-slim
EXPOSE 8080

COPY --from=build /target/todolist-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]