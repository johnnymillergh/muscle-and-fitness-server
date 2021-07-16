![Muscle and Fitness Server Social Image](https://raw.githubusercontent.com/johnnymillergh/MaterialLibrary/master/muscle-and-fitness/muscle-and-fitness-server-social-image.png)
[![GitHub release](https://img.shields.io/github/release/johnnymillergh/muscle-and-fitness-server.svg)](https://github.com/johnnymillergh/muscle-and-fitness-server/releases)
[![Build Status](https://travis-ci.com/johnnymillergh/muscle-and-fitness-server.svg?branch=master)](https://travis-ci.com/johnnymillergh/muscle-and-fitness-server)
[![GitHub issues](https://img.shields.io/github/issues/johnnymillergh/muscle-and-fitness-server)](https://github.com/johnnymillergh/muscle-and-fitness-server/issues)
[![GitHub forks](https://img.shields.io/github/forks/johnnymillergh/muscle-and-fitness-server)](https://github.com/johnnymillergh/muscle-and-fitness-server/network)
[![GitHub stars](https://img.shields.io/github/stars/johnnymillergh/muscle-and-fitness-server)](https://github.com/johnnymillergh/muscle-and-fitness-server)
[![GitHub license](https://img.shields.io/github/license/johnnymillergh/muscle-and-fitness-server)](https://github.com/johnnymillergh/muscle-and-fitness-server/blob/master/LICENSE)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/johnnymillergh/muscle-and-fitness-server.svg?style=popout)](https://github.com/johnnymillergh/muscle-and-fitness-server)
[![GitHub repo size](https://img.shields.io/github/repo-size/johnnymillergh/muscle-and-fitness-server.svg)](https://github.com/johnnymillergh/muscle-and-fitness-server)
[![Twitter](https://img.shields.io/twitter/url/https/github.com/johnnymillergh/muscle-and-fitness-server?style=social)](https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fjohnnymillergh%2Fmuscle-and-fitness-server)

# Muscle and Fitness Server

**Muscle and Fitness Server** a Spring Cloud microservice based, back-end server for managing data of muscle and fitness.

[Official Docker Image](https://hub.docker.com/u/ijohnnymiller)

## Features

Here is the highlights of **Muscle and Fitness Server**:

1. Inherited from the most modern and newest Spring frameworks:

   `org.springframework.boot:spring-boot-starter-parent` - [![Spring Boot](https://maven-badges.herokuapp.com/maven-central/org.springframework.boot/spring-boot-starter-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.springframework.boot/spring-boot-starter-parent/)
   `org.springframework.cloud:spring-cloud-dependencies` - [![Spring Cloud](https://maven-badges.herokuapp.com/maven-central/org.springframework.cloud/spring-cloud-dependencies/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.springframework.cloud/spring-cloud-dependencies/)

2. [Spring Cloud](https://spring.io/projects/spring-cloud) Feature:

   - Consul - Service registration and discovery.
   - Spring Cloud Gateway - API Gateway on top of Spring WebFlux.  Provide a simple, yet effective way to route to APIs and provide cross cutting concerns to them such as: security, monitoring/metrics, and resiliency.
   - Spring Cloud OpenFeign - Declarative REST Client: Feign creates a dynamic implementation of an interface decorated with JAX-RS or Spring MVC annotations. Enhanced connection by Okhttp 3.
   - Spring Boot Admin - Admin UI for administration of spring boot applications.
   - Zipkin - a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in service architectures.

3. Secured API. RBAC control by API gateway and Auth Center. JWT authentication, and RBAC authorization.

4. Higher-level packaging other libraries: `Spring Cloud Starter`, auto configuration for Spring Cloud services.

5. MySQL cluster support. Multi data source. Dynamic SQL read-write isolation. MyBatis-Plus is the integrated ORM library.

6. Redis 6.x support.

7. Docker, Rancher Kubernetes support. Google JIB for building Docker container images.

8. OSS service, based on Minio and SFTP integration.

9. STOMP over WebSocket (SockJS), real time messaging, based on RabbitMQ STOMP message broker.

10. Quartz support. Distributed job scheduling, based on JDBC.

11. Multi-environment support.

12. API visualization. Enhanced Swagger API documentation.

13. Log compression. ELK log aggregation.

14. Request log.

15. Method Argument Validation Aspect.

16. Docker container log persistence.

17. Startup statistics.

18. Customized startup banner.

## Usage

1. Clone or download this project.

   ```shell
   $ git clone https://github.com/johnnymillergh/muscle-and-fitness-server.git
   ```

2. Build with newest Intellij IDEA.

3. Click the green triangle to Run.

## Useful Commands

### Maven

1. Compile:

   ```shell
   $ mvn clean validate compile --batch-mode --show-version --quiet -f pom.xml
   ```

2. Package:

   ```shell
   $ mvn clean package --batch-mode --show-version --quiet -f pom.xml
   ```

3. Set Version:

   ```sh
   $ mvn versions:set -DgenerateBackupPoms=false -f pom.xml
   ```

4. Build Docker Images:

   ```shell
   $ mvn clean verify --batch-mode --show-version --quiet -f pom.xml
   ```

### Conventional Changelog CLI

1. Install global dependencies (optional if installed):

   ```
   npm install -g conventional-changelog-cli
   ```

2. This will *not* overwrite any previous changelogs. The above generates a changelog based on commits since the last semver tag that matches the pattern of "Feature", "Fix", "Performance Improvement" or "Breaking Changes".

   ```
   conventional-changelog -p angular -i CHANGELOG.md -s
   ```

3. If this is your first time using this tool and you want to generate all previous changelogs, you could do:

   ```
   conventional-changelog -p angular -i CHANGELOG.md -s -r 0
   ```

## CI (Continuous Integration)

- [Travis CI](https://travis-ci.com/github/johnnymillergh/media-streaming) is for publishing Docker Hub images of SNAPSHOT and RELEASE.
- [GitHub Actions](https://github.com/johnnymillergh/media-streaming/actions) is for checking dependency updates and tests.

## Maintainers

[@johnnymillergh](https://github.com/johnnymillergh).

## Contributing

Feel free to dive in! [Open an issue](https://github.com/johnnymillergh/spring-cloud-tutorial/issues/new).

### Contributors

This project exists thanks to all the people who contribute. 

- Johnny Miller [[@johnnymillergh](https://github.com/johnnymillergh)]
- …


### Sponsors

Support this project by becoming a sponsor. Your logo will show up here with a link to your website. [[Become a sponsor](https://become-a-sponsor.org)]

## License

[Apache License](https://github.com/johnnymillergh/exrx-net-crawler-server/blob/master/LICENSE) © Johnny Miller

2020 - Present

