# Spring Boot 3 Example

공공 데이터(아파트 실거래/행정동 코드)와 외부 서비스(카카오맵, 네이버 부동산 뉴스)를 활용해 **아파트 정보 조회, 관심 매물 관리, 게시판, 회원 기능**을 제공하는 Spring Boot 기반 웹 애플리케이션입니다.

---

## 1) 기술 스택 (Spring Boot 3 업그레이드)

### Backend
- **Java 17**
- **Spring Boot 3.3.2**
  - Spring Web (MVC)
  - Embedded Tomcat 10
  - Devtools
- **MyBatis Spring Boot Starter 3.0.3** (XML Mapper 기반)
- **MariaDB JDBC Driver**
- **Spring AOP**
- **JWT (jjwt 0.9.1)**
- **OpenAPI (springdoc-openapi 2.6.0)**
- **JSoup 1.14.2** (뉴스 크롤링)

### View / Frontend (서버 렌더링)
- **JSP + JSTL (Jakarta JSTL)**
- 정적 리소스: HTML/CSS/JavaScript
- UI 라이브러리: Bootstrap, AOS, Swiper, Boxicons 등(프로젝트 내 static vendor assets)

### Data / Infra
- **MariaDB 11 (Docker Compose)**
- SQL 스키마/초기 데이터: `db.sql` (컨테이너 초기 실행 시 자동 반영)

---

## 2) 프로젝트 구조

```text
.
├── docker-compose.yml      # MariaDB 컨테이너 구성 및 db.sql 자동 로딩
├── db.sql                  # DB 스키마/초기 데이터
├── scripts
│   └── run-local.sh        # DB 기동 + Spring Boot 실행 통합 스크립트
└── src
    ├── main/java/com/ssafy/live
    │   ├── config           # WebMvc, OpenAPI 설정
    │   ├── controller       # 사용자/아파트/게시판/관리자/뉴스 API 및 페이지 라우팅
    │   ├── dto              # 도메인 모델
    │   ├── interceptor      # 로그인 인증 인터셉터
    │   └── model            # Mapper/Service 계층
    ├── main/resources
    │   ├── application.properties
    │   ├── mapper/*.xml
    │   └── static
    └── main/webapp/WEB-INF/views/*.jsp
```

---

## 3) 실행 방법

### 3-1. 사전 준비

아래 도구가 설치되어 있어야 합니다.

1. **JDK 17**
2. **Maven 3.8+**
3. **Docker / Docker Compose**

버전 확인:

```bash
java -version
mvn -v
docker --version
docker compose version
```

### 3-2. 원클릭 실행 (권장)

아래 스크립트 하나로
1) MariaDB 컨테이너 실행
2) `db.sql` 자동 반영 확인
3) Spring Boot 실행
을 순서대로 처리합니다.

```bash
bash scripts/run-local.sh
```

실행 후 접속:
- **http://localhost:8080/**
- OpenAPI UI: **http://localhost:8080/swagger-ui/index.html**

### 3-3. 수동 실행

#### 1) DB만 먼저 실행

```bash
docker compose up -d mariadb
```

> `docker-compose.yml`에서 `./db.sql`을 `/docker-entrypoint-initdb.d/01-db.sql`로 마운트하므로, **최초 컨테이너 생성 시 자동 초기화**됩니다.

#### 2) Spring Boot 실행

```bash
mvn spring-boot:run
```

필요 시 환경 변수로 DB 접속 정보 오버라이드:

```bash
export SPRING_DATASOURCE_URL="jdbc:mariadb://127.0.0.1:3306/homedb?serverTimezone=UTC"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="123456"
mvn spring-boot:run
```

---

## 4) 주요 설정

`src/main/resources/application.properties`

```properties
server.port=${SERVER_PORT:8080}
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mariadb://127.0.0.1:3306/homedb?serverTimezone=UTC}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:123456}
```

---

## 5) 트러블슈팅

### Q1. DB 초기화가 안 된 것 같습니다.
- 기존 볼륨이 남아 있으면 init SQL이 재실행되지 않습니다.
- 아래로 볼륨 제거 후 다시 실행하세요.

```bash
docker compose down -v
docker compose up -d mariadb
```

### Q2. Spring Boot가 DB 연결 실패합니다.
- `docker compose ps`로 `whereis-mariadb` 상태 확인
- `docker logs whereis-mariadb`로 기동 오류 확인
- 포트 충돌 여부 확인 (`3306`)

### Q3. Swagger 경로가 다릅니다.
- Spring Boot 3 + springdoc 기준 UI 경로는 `/swagger-ui/index.html` 입니다.

