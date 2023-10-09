# syntax=docker/dockerfile:1

FROM gradle:8.4.0-jdk17 as build
RUN mkdir /tmp/platform
COPY ../ /tmp/platform/
WORKDIR /tmp/platform
RUN ./gradlew clean build jar

FROM amazoncorretto:17-alpine3.18
RUN mkdir /opt/app
COPY --from=build /tmp/platform/posts-dump-backend/build/libs/posts-dump-backend-*.jar /opt/app/posts-dump-backend.jar
WORKDIR /opt/app

ENV APP_LOGS_DIR="/var/app/logs"
ENTRYPOINT ["java", "-jar", "posts-dump-backend.jar", "-DAPP_LOGS_DIR=$APP_LOGS_DIR"]
