## Project Description

Message driven platform implementing process for `https://jsonplaceholder.typicode.com` REST
resources dump.

## Tech Stack

* Kotlin
* `IN PROGRESS` ArrowKT / FlowKT
* Google Guice
* RabbitMQ
* MinIO storage / AWS SDK 2
* Fuel HTTP client / Moshi JSON
* Kotlin Logger / Logback
* Kotest / Mockk / BDD
* Gradle v8.4 (Wrapper)
* Docker v23.0.5 / Compose v2.17.3
* `TODO` Kubernetes
* `TODO` Kafka
* `TODO` Spock tests

## Run Application

Run following commands in project root directory:

```bash 
docker compose -f ./deployment/dev/compose.yaml up &
```

* Create RabbitMQ queue named `pd-requests` [(link)](http://localhost:15672).
* Create Minio bucket named `posts` [(link)](http://localhost:9001).

```bash
./gradlew clean build
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

export FILE_SYSTEM_BASE_DIRECTORY=dev/requests

java -jar -DAPP_LOGS_DIR=./dev/logs ./posts-dump-backend/build/libs/posts-dump-backend-0.0.1-SNAPSHOT-all.jar
```

Optionally, build `posts-dump-backend` image from project root:

```bash
docker build -f posts-dump-backend/Dockerfile . -t dev-pd-backend
```

```bash
docker run --env-file posts-dump-backend/.env --network host dev-pd-backend
```

## Local Environment

Spinning up local environment requires creating resources and restarting `pd-backend` service.

```bash
docker compose -f ./deployment/local/compose.yaml up &
```

## Next Features

1. `ARCHITECTURE` Apply Coroutines.
2. `DEPLOYMENT` Create boot scripts for queue/buckets components.
3. `CONFIGURATION` Add shutdown hooks for stopping threads/closing resources from e.g. logger,
   rabbitmq.
4. `DEPLOYMENT` Decrease Docker image size by using JRE instead of JDK.
5. `CONFIGURATION` Workers fine tuning for message broker.

### Useful Links

- https://richardstartin.github.io/posts/lifecycle-management-with-guice-provision-listeners
- https://careers.wolt.com/en/blog/tech/how-to-reduce-jvm-docker-image-size
- https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/
- https://logback.qos.ch/manual/configuration.html
