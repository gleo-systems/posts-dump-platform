# syntax=docker/dockerfile:1

# No jdk17-alpine version for MacOS
FROM gradle:8.4.0-jdk17 as build
WORKDIR /tmp/app
COPY ./ ./
RUN gradle clean build --no-daemon

# Replace with 17-jre-alpine
FROM amazoncorretto:17-alpine3.18
RUN mkdir /opt/app && mkdir -p /var/app/logs
WORKDIR /opt/app
COPY --from=build /tmp/app/posts-dump-backend/build/libs/posts-dump-backend-*-all.jar ./application.jar
RUN addgroup -S app && adduser -S -G app app
RUN chown -R app:app /opt/app && chown -R app:app /var/app
USER app

ENV APP_LOGS_DIR="/var/app/logs"
ENTRYPOINT ["java", "-jar", "application.jar", "-DAPP_LOGS_DIR=$APP_LOGS_DIR"]
