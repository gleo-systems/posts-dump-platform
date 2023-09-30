# syntax=docker/dockerfile:1

FROM maven:3.9.4-amazoncorretto-17-al2023 as build
RUN mkdir /tmp/app
COPY . /tmp/app/
WORKDIR /tmp/app
RUN mvn clean package

FROM amazoncorretto:17-alpine3.18
RUN mkdir /opt/app
COPY --from=build /tmp/app/target/posts-dump-backend-*.jar /opt/app/posts-dump-backend.jar
WORKDIR /opt/app

ENV APP_LOGS_DIR="/var/app/logs"
ENTRYPOINT ["java", "-jar", "posts-dump-backend.jar", "-DAPP_LOGS_DIR=$APP_LOGS_DIR"]
