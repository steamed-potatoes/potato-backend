# Potato Backend 

![Generic badge](https://img.shields.io/badge/version-0.3.4-orange.svg)
[![codecov](https://codecov.io/gh/steamed-potatoes/potato-backend/branch/develop/graph/badge.svg?token=ACoWRzqGBl)](https://codecov.io/gh/steamed-potatoes/potato-backend)

## Test Server
- 기능 테스트는 이곳에서 하실 수 있습니다. (실제 UI와 관련 X)
  
https://potato-front.netlify.app/

## Introduction
### 동아리의 다양한 일정들을 한 곳에서 관리하는 웹 서비스
동아리와 관련된 신입 모집, 행사, 이벤트 등을 한 곳에서 관리하고 볼 수 있는 웹 서비스.
### 동아리 운영진은요...!
- 새로운 동아리를 등록하고, 관리할 수 있어요.
- 동아리에서 신규 모집, 행사, 이벤트 등 홍보를 언제든 할 수 있어요.
- 동아리원들의 가입 신청을 받고, 동아리원들을 관리할 수 있어요.
  
### 동아리에 들어가고 싶은 학우들은요...!
- 인기 있는 동아리는 무엇인지, 어떤 동아리들이 있는지 한눈에 확인할 수 있어요.
- 더이상 여러 곳에서 신규 모집 등 이정을 일일이 확인할 필요 없이 동아리에서 업로드한 홍보글들을 한 곳에서 확인할 수 있어요!
- 누구든지 해당 게시글에서 댓글을 통해 커뮤니케이션 할 수 있어요.
- 내가 참여하고 있는 동아리의 일정, 행사 등을 한곳에서 확인할 수 있어요.
- 내가 관심있어하는 동아리를 팔로우해두고, 신입 모집 게시글이 올라오면 쉽게 확인할 수 있어요.


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
- Redis (Session)
- Docker-compose
- Nginx

### Infra (Local)
- H2 Inmemory DB
- Redis Embedded DB (Session)

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
