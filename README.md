## Project Description

Message triggered dump of `https://jsonplaceholder.typicode.com` posts and comments resources.

## Tech Stack

* Kotlin
* Google Guice
* RabbitMQ
* MinIO storage / AWS SDK 2
* Kotlin Logger/Logback
* `TODO` Go async
* `TODO` Arrow !
* `TODO` Kubernetes
* `TODO` Kafka

## Environment configuration

Check `.env` file.

## Next steps

1. `FEATURE` Dump (downloading) JSONPlaceholder posts/comments items using Coroutines and storing in a bucket.
2. `DEPLOYMENT` Create boot scripts for queue/buckets components and change default credentials.
3. `CONFIGURATION` Add shutdown hooks if necessary for stopping threads of: logger, rabbitmq.
4. `DEPLOYMENT` Decrease Docker image size by using JRE instead of JDK and configure non-root Docker user.
5. `DEPLOYMENT` Switch to Gradle (take care of maven shading warnings) and from KAPT to KSP processor.
6. `CONFIGURATION` Improve logging: format, rolling policy and decide if file logger is needed.

### Useful Links

- https://richardstartin.github.io/posts/lifecycle-management-with-guice-provision-listeners
- https://careers.wolt.com/en/blog/tech/how-to-reduce-jvm-docker-image-size
- https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/
- https://logback.qos.ch/manual/configuration.html
