# Potato Backend 

![Generic badge](https://img.shields.io/badge/version-0.2.3-orange.svg)
[![codecov](https://codecov.io/gh/steamed-potatoes/potato-backend/branch/develop/graph/badge.svg?token=ACoWRzqGBl)](https://codecov.io/gh/steamed-potatoes/potato-backend)

---
## Development Server

[API Server](https://api.pmarket.space/ping)

![API Server](https://img.shields.io/website?down_message=OFF&style=flat-square&up_message=ON&url=https%3A%2F%2Fapi.pmarket.space%2Fping)

[Admin Server](https://test.pmarket.space/ping)

![Admin Server](https://img.shields.io/website?down_message=OFF&style=flat-square&up_message=ON&url=https%3A%2F%2Ftest.pmarket.space%2Fping)

---

## Technologies

### Language & Framework
- Language: Java 11
- Framework: Spring Boot 2.3
- Web Framework: Spring Web MVC + Spring WebFlux (WebClient)
- Data Access: Spring Data JPA (Hibernate) + QueryDSL 4.3
- Build: Gradle 6.7
- Unit/Integration Testing: Junit 5

### Infra (Production Server)
- TODO

### Infra (Development Server)
- AWS EC2
- AWS RDS (MariaDB 10.4), flyway 6.4
- Docker-compose
- Nginx

### Infra (Local)
- H2 InMemory DB

### CI/CD
- GitHub Action CI/CD

---

## Getting Started
```shell
git clone https://github.com/steamed-potatoes/potato-backend
```

### case 1) with gradlew
```shell
# Build
./gradlew clean build

# API Server
java -jar potato-api/build/libs/potato-api.jar  

# Admin Server
java -jar potato-admin/build/libs/potato-admin.jar
```
### case 2) with docker-compose
```shell
docker-compose up --build
```

---

## Contacts
- will.seungho@gmail.com
