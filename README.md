## Project Description

Message driven platform implementing process for `https://jsonplaceholder.typicode.com` REST resources dump.

## Tech Stack

* Kotlin
* `IN PROGRESS` Arrow
* Google Guice
* RabbitMQ
* MinIO storage / AWS SDK 2
* Fuel http client / Moshi JSON
* Kotlin Logger / Logback
* Maven v3.9.3
* Docker v23.0.5 / Compose v2.17.3
* `TODO` Kubernetes
* `TODO` Kafka

## Run Application

Run following commands in project root directory (Unix OS):

```bash 
docker compose -f ./dev/docker/compose.yaml up &
```

```bash
mvn clean package -f ./posts-dump-backend/pom.xml
```

```bash
export JSON_PLACEHOLDER_API_URL=http://jsonplaceholder.typicode.com

export MESSAGE_BROKER_USERNAME=guest
export MESSAGE_BROKER_PASSWORD=guest
export MESSAGE_BROKER_HOSTNAME=localhost
export MESSAGE_BROKER_CONSUMER_TAG=posts-dump-backend
export MESSAGE_BROKER_CHANNEL_NAME=pd-requests
export MESSAGE_BROKER_CHANNEL_AUTOACK=false

export STORAGE_REGION=eu-west-1
export STORAGE_ACCESS_KEY_ID=minioadmin
export STORAGE_SECRET_ACCESS_KEY=minioadmin
export STORAGE_API_URL=http://localhost:9000
export STORAGE_BASE_LOCATION=posts

java -jar ./posts-dump-backend/target/posts-dump-backend-0.0.1-SNAPSHOT.jar -DAPP_LOGS_DIR=./dev/logs
```

## Testing Environment

Spinning up testing environment requires:

* creating RabbitMQ queue `pd-requests`,
* creating Minio bucket `posts`,
* restarting service `pd-backend`.

```bash
docker compose -f ./deployment/test/docker/compose.yaml up &
```

## Next Features

1. `ARCHITECTURE` Apply Coroutines.
2. `DEPLOYMENT` Create boot scripts for queue/buckets components and change TEST env credentials.
3. `CONFIGURATION` Add shutdown hooks for stopping threads/closing resources from e.g. logger, rabbitmq.
4. `DEPLOYMENT` Decrease Docker image size by using JRE instead of JDK and configure non-root Docker user.
5. `DEPLOYMENT` Switch to Gradle (take care of maven shading warnings) and switch from KAPT to KSP processor.
6. `CONFIGURATION` Improve logging: format, rolling policy and decide if file logger is needed.
7. `CONFIGURATION` Workers fine tuning for message broker.

### Useful Links

- https://richardstartin.github.io/posts/lifecycle-management-with-guice-provision-listeners
- https://careers.wolt.com/en/blog/tech/how-to-reduce-jvm-docker-image-size
- https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/
- https://logback.qos.ch/manual/configuration.html
