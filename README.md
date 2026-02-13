# 🏡 WhereIsMyHome (Whereis-BE)

공공 데이터(아파트 실거래/행정동 코드)와 외부 서비스(카카오맵, 네이버 부동산 뉴스)를 활용해 **아파트 정보 조회, 관심 매물 관리, 게시판, 회원 기능**을 제공하는 Spring Boot 기반 웹 애플리케이션입니다.

---

## 1) 기술 스택

### Backend
- **Java 17**
- **Spring Boot 2.7.5**
  - Spring Web (MVC)
  - Embedded Tomcat
  - Devtools
- **MyBatis 2.2.2** (XML Mapper 기반)
- **MariaDB JDBC Driver**
- **Spring AOP**
- **JWT (jjwt 0.9.1)**
- **Swagger/Springfox 3.0.0**
- **JSoup 1.14.2** (뉴스 크롤링)

### View / Frontend (서버 렌더링)
- **JSP + JSTL**
- 정적 리소스: HTML/CSS/JavaScript
- UI 라이브러리: Bootstrap, AOS, Swiper, Boxicons 등(프로젝트 내 static vendor assets)

### Data / Infra
- **MariaDB**
- SQL 스키마/초기 데이터: `db.sql`

---

## 2) 프로젝트 구조

```text
src/main/java/com/ssafy/live
├── config           # WebMvc, Swagger 설정
├── controller       # 사용자/아파트/게시판/관리자/뉴스 API 및 페이지 라우팅
├── dto              # 도메인 모델
├── interceptor      # 로그인 인증 인터셉터
├── model
│   ├── mapper       # MyBatis Mapper 인터페이스
│   └── service      # 비즈니스 로직
└── aop              # 로깅 등 공통 관심사

src/main/resources
├── application.properties  # 서버/DB/MyBatis/ViewResolver 설정
├── mapper/*.xml            # MyBatis SQL 매퍼
└── static                  # CSS/JS/이미지 등 정적 파일

src/main/webapp/WEB-INF/views
└── *.jsp                   # 화면(JSP)
```

---

## 3) 로컬 실행 방법 (상세)

### 3-1. 사전 준비

아래가 설치되어 있어야 합니다.

1. **JDK 17**
2. **Maven 3.8+** (또는 프로젝트에 맞는 Maven)
3. **MariaDB 10.x**

버전 확인 예시:

```bash
java -version
mvn -v
mysql --version
```

---

### 3-2. 데이터베이스 준비

1. MariaDB 서버 실행
2. 루트(또는 권한이 있는 계정)로 접속 후 스키마/테이블/샘플 데이터 생성

```bash
mysql -u root -p < db.sql
```

기본 SQL은 `homedb` 데이터베이스를 생성/사용합니다.

---

### 3-3. 애플리케이션 설정

`src/main/resources/application.properties`의 DB 접속 정보를 본인 환경에 맞게 수정합니다.

```properties
spring.datasource.url=jdbc:mariadb://127.0.0.1/homedb?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
```

> 기본 포트는 `8080`입니다.

필요 시 다음도 확인하세요.
- `server.port`
- `mybatis.mapper-locations`
- JSP ViewResolver (`spring.mvc.view.prefix/suffix`)

---

### 3-4. 애플리케이션 실행

#### 방법 A) Maven으로 바로 실행

```bash
mvn spring-boot:run
```

#### 방법 B) 패키징 후 실행

```bash
mvn clean package
java -jar target/live2-0.0.1-SNAPSHOT.jar
```

실행 후 접속:
- **http://localhost:8080/**

---

### 3-5. 주요 기능 확인 포인트

- 회원가입/로그인/회원정보 관리
- 아파트 조회 및 관심 매물 등록
- 게시판 CRUD
- 네이버 부동산 뉴스 크롤링 정보 확인
- 카카오맵 기반 위치 시각화

> 카카오맵 API 사용 화면은 JSP 내 스크립트 키 설정 상태에 따라 동작 여부가 달라질 수 있습니다.

---

## 4) 개발/운영 참고 사항

- 본 프로젝트는 **JSP 서버 렌더링 기반** 구조입니다.
- MyBatis XML SQL을 사용하므로, 쿼리 수정 시 `src/main/resources/mapper/*.xml`을 함께 관리하세요.
- DB 계정/비밀번호는 로컬 개발 환경 기준이므로 운영 배포 시 환경변수 또는 별도 설정 파일로 분리하는 것을 권장합니다.

---

## 5) 빠른 트러블슈팅

### Q1. 실행 시 DB 연결 오류가 발생합니다.
- MariaDB가 실행 중인지 확인
- `application.properties`의 URL/계정/비밀번호 확인
- `homedb` 및 테이블이 실제 생성되었는지 확인 (`db.sql` 재적용)

### Q2. JSP 관련 오류가 발생합니다.
- `tomcat-embed-jasper`, `jstl` 의존성이 정상 다운로드되었는지 확인
- 빌드 캐시 삭제 후 재빌드 (`mvn clean package`)

### Q3. 지도/외부 데이터가 비정상입니다.
- 카카오맵 API 키 유효성/도메인 등록 여부 확인
- 외부 사이트 구조 변경 시 JSoup 크롤링 로직 점검

---

## 6) 실행 명령어 요약

```bash
# 1) DB 초기화
mysql -u root -p < db.sql

# 2) 앱 실행
mvn spring-boot:run

# 3) 패키징 실행
mvn clean package
java -jar target/live2-0.0.1-SNAPSHOT.jar
```
