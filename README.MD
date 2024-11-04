## Project Name

AFT-Backend-Task

## Project Description

이 프로젝트는 멀티모듈 구조로 구성되어 있으며, api, auth, domain 모듈로 나뉩니다.  
api 모듈은 auth와 domain 모듈에 의존하고 있으며, 모든 인증 관련 기능은 auth 모듈에서 JWT 토큰 기반으로 인증 및 인가를 수행합니다.  
domain 모듈은 엔티티와 리포지토리 등 데이터베이스와 관련된 작업을 관리하며, api 모듈은 비즈니스 로직에 집중할 수 있도록 가볍게 설계되었습니다.

## Technologies Used

- Java 17
- Spring Boot 3.3.5
- Gradle
- JPA
- H2 Database
- QueryDSL
- JWT
- Redis

## Directory Structure

```
├── HELP.md
├── README.md
├── aft-api
│   ├── build.gradle
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── api
│       │   │               ├── ApiApplication
│       │   │               ├── board
│       │   │               │   ├── controller
│       │   │               │   ├── scheduler
│       │   │               │   └── service
│       │   │               ├── config
│       │   │               ├── exception
│       │   │               ├── interceptor
│       │   │               └── user
│       │   │                   ├── controller
│       │   │                   └── service
│       │   └── resources
│       │       └── application.yml
├── aft-auth
│   ├── build.gradle
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── auth
│       │   │               ├── config
│       │   │               ├── jwt
│       │   │               └── service
│       │   └── resources
├── aft-domain
│   ├── build.gradle
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── domain
│       │   │               ├── config
│       │   │               ├── entity
│       │   │               ├── enums
│       │   │               ├── payload
│       │   │               ├── repository
│       │   │               └── util
│       │   └── resources
│       │       ├── data.sql
│       │       └── schema.sql
├── build.gradle
└── settings.gradle
```

- aft-api: API 서버 모듈 (controller, service, config 등)
- aft-auth: 인증 서버 모듈 (security, jwt, config 등)
- aft-domain: 도메인 모듈 (entity, repository, enums, payload 등)

## How to Run

1. 저장소 클론
    - git clone <repository-url>
2. 프로젝트 루트 디렉토리로 이동
    - cd aft-backend-task
3. 의존성 설치
    - ./gradlew clean build
4. Redis 서버가 실행 중인지 확인
    - 로컬에서 Redis 서버를 시작하거나 클라우드 기반의 Redis 서비스가 실행 중인지 확인
5. 어플리케이션 실행
    - ./gradlew :aft-api:bootRun

서버 실행시 data.sql, schema.sql 파일을 참조하여 H2 데이터베이스에 테이블을 생성하고 초기 데이터가 삽입됩니다.

````
어드민 계정 정보
- userId: admin
- password: Abcd1234!
````

## API Documentation

- postman api 문서: https://documenter.getpostman.com/view/18393194/2sAY4ydfWF#c1abf551-6385-49e1-afd3-f4816b8297c2

## 고민한 점

- 멀티모듈 프로젝트 구성이 처음이라 프로젝트 구조를 어떻게 설계해야 할지 고민했습니다.
- 처음 시작할 때 도메인 중심의 모듈을 구성을 해야하나 API 중심의 모듈을 구성해야하나 고민했지만 MSA 프로젝트가 아닌 단일 서버에서 동작하는 서비스이기 때문에 API 중심의 모듈을 구성하기로 결정했습니다
  그로인해 설계 단계에서 많은 시간이 소요되었습니다.
- Redis를 통해 조회수를 관리하는 방식으로 성능을 높이고자 했지만, Redis에 대한 경험이 부족해 초기 구현 방법과 효율적인 데이터 관리를 고민하는 데 어려움이 있었습니다.

## 아쉬운 점

- 개발의 완성도를 높이기 위해 테스트 코드를 작성하려 했으나, 개인 사정으로 인해 시간이 부족해 테스트 코드 작성에 집중할 수 없었습니다.
  기회가 된다면 테스트 코드 작성에 더욱 신경쓰고 싶습니다.
