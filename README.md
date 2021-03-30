# Potato Backend

![Generic badge](https://img.shields.io/badge/version-0.1.2-brightgreen.svg)
[![codecov](https://codecov.io/gh/steamed-potatoes/potato-backend/branch/develop/graph/badge.svg?token=ACoWRzqGBl)](https://codecov.io/gh/steamed-potatoes/potato-backend)

## Health Check

[API Server](https://api.pmarket.space/ping)

[Admin Server](https://test.pmarket.space/ping)

## Tech Stack

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
- Github Action CI/CD
