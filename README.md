# Potato Backend

![Generic badge](https://img.shields.io/badge/version-0.1.4-brightgreen.svg)
[![codecov](https://codecov.io/gh/steamed-potatoes/potato-backend/branch/develop/graph/badge.svg?token=ACoWRzqGBl)](https://codecov.io/gh/steamed-potatoes/potato-backend)

## Health Check (Development Server)

[API Server](https://api.pmarket.space/ping)

[Admin Server](https://test.pmarket.space/ping)

## Technologies

### Language & Framework
- Language: Java 11
- Framework: Spring Boot 2.3
- Web Framework: Spring Web MVC + Spring WebFlux (WebClient)
- Data Access: Spring Data JPA + Hibernate JPA + QueryDSL
- Build: Gradle
- Unit/Integration Testing: Junit5

### Infra (Production Server)
- TODO

### Infra (Development Server)
- AWS EC2
- AWS RDS (MariaDB)
- Docker-compose
- Nginx

### CI/CD
- GitHub Action CI/CD

## Getting Started
```shell
git clone https://github.com/steamed-potatoes/potato-backend
```

### case1. with gradlew
```shell
./gradlew clean build
java -jar muyaho-api/build/libs/muyaho-api-0.0.1-SNAPSHOT.jar 
```
### case2. with docker-compose
```shell
docker-compose up --build
```

## Contact
will.seungho@gmail.com
