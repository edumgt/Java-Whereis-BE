# WhereIsMyHome — MSA 배포 실습 가이드

> **목표**: Spring Boot 모놀리스 애플리케이션을 5개의 독립적인 마이크로서비스로 분리하고, Docker 이미지로 빌드한 뒤, Kubernetes(k8s) 클러스터에 각 서비스를 독립된 Pod로 배포하여 NodePort 방식으로 외부에 노출합니다.

---

## 📐 아키텍처 개요

```
┌─────────────────────────────────────────────────────────────────┐
│                    Kubernetes Cluster                           │
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ user-service │  │  apt-service │  │board-service │         │
│  │  Port: 8081  │  │  Port: 8082  │  │  Port: 8083  │         │
│  │NodePort:30081│  │NodePort:30082│  │NodePort:30083│         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
│         │                  │                  │                  │
│  ┌──────────────┐  ┌──────────────┐           │                 │
│  │admin-service │  │ news-service │           │                 │
│  │  Port: 8084  │  │  Port: 8085  │           │                 │
│  │NodePort:30084│  │NodePort:30085│           │                 │
│  └──────┬───────┘  └──────────────┘           │                 │
│         │                                      │                 │
│  ┌──────┴──────────────────────────────────────┘                │
│  │              MariaDB (ClusterIP: 3306)                       │
│  └──────────────────────────────────────────────────────────────┘
└─────────────────────────────────────────────────────────────────┘
         ↑                ↑                ↑               ↑
    :30081~30085     (NodePort로 외부 접근 가능)
```

### 서비스 포트 매핑

| 서비스 | 담당 기능 | 컨테이너 포트 | NodePort | API 경로 예시 |
|---|---|---|---|---|
| `user-service` | 회원가입·로그인·정보수정·탈퇴 | 8081 | **30081** | `/users/login` |
| `apt-service` | 아파트 조회·관심 매물·지역코드 | 8082 | **30082** | `/apts`, `/dong-code/sido` |
| `board-service` | 게시판 CRUD·검색 | 8083 | **30083** | `/board` |
| `admin-service` | 회원 목록·권한 관리 | 8084 | **30084** | `/admin` |
| `news-service` | 부동산 뉴스 크롤링 | 8085 | **30085** | `/news/all` |
| `mariadb` | DB (클러스터 내부 전용) | 3306 | — | — |

---

## 🧰 사전 준비 (Prerequisites)

아래 도구가 설치되어 있어야 합니다.

| 도구 | 권장 버전 | 확인 명령 |
|---|---|---|
| Java (JDK) | 17 이상 | `java -version` |
| Apache Maven | 3.8 이상 | `mvn -version` |
| Docker | 24 이상 | `docker --version` |
| Docker Compose | v2.x (Compose V2) | `docker compose version` |
| kubectl | 1.28 이상 | `kubectl version --client` |
| Kubernetes 클러스터 | minikube / kubeadm / k3s 등 | `kubectl cluster-info` |

> **로컬 환경 추천**: [minikube](https://minikube.sigs.k8s.io/docs/start/) 또는 [Docker Desktop K8s](https://docs.docker.com/desktop/kubernetes/)

---

## 📁 프로젝트 디렉터리 구조

```
Java-Whereis-BE/
├── src/                        # 원본 모놀리스 (참고용)
├── user-service/               # 사용자 마이크로서비스
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── apt-service/                # 아파트/지역코드 마이크로서비스
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── board-service/              # 게시판 마이크로서비스
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── admin-service/              # 관리자 마이크로서비스
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── news-service/               # 뉴스 마이크로서비스
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── docker-compose.yml          # 로컬 전체 실행용
├── k8s/                        # Kubernetes 매니페스트
│   ├── mariadb.yaml
│   ├── user-service.yaml
│   ├── apt-service.yaml
│   ├── board-service.yaml
│   ├── admin-service.yaml
│   └── news-service.yaml
└── db.sql                      # DB 초기화 스크립트
```

---

## 🐳 Part 1: Docker Compose로 로컬 실행 (개발 환경)

> Docker Compose는 여러 컨테이너를 한 번에 빌드·실행하는 로컬 개발 도구입니다. K8s 배포 전 빠르게 전체 동작을 확인할 때 사용합니다.

### Step 1-1. 저장소 클론

```bash
git clone https://github.com/edumgt/Java-Whereis-BE.git
cd Java-Whereis-BE
```

### Step 1-2. 전체 서비스 빌드 및 실행

```bash
# 모든 서비스 이미지 빌드 후 컨테이너 시작 (백그라운드)
docker compose up --build -d
```

> 처음 실행 시 Maven 의존성 다운로드로 수 분이 소요됩니다.

### Step 1-3. 실행 상태 확인

```bash
docker compose ps
```

예상 출력:
```
NAME                      STATUS          PORTS
whereis-mariadb           Up (healthy)    0.0.0.0:3306->3306/tcp
whereis-user-service      Up              0.0.0.0:8081->8081/tcp
whereis-apt-service       Up              0.0.0.0:8082->8082/tcp
whereis-board-service     Up              0.0.0.0:8083->8083/tcp
whereis-admin-service     Up              0.0.0.0:8084->8084/tcp
whereis-news-service      Up              0.0.0.0:8085->8085/tcp
```

### Step 1-4. 동작 확인

```bash
# 사용자 서비스 — 회원가입
curl -X POST http://localhost:8081/users/join \
  -H "Content-Type: application/json" \
  -d '{"userId":"testuser","userName":"테스터","userPwd":"pass1234","emailId":"test","emailDomain":"ssafy.com"}'

# 아파트 서비스 — 시도 목록 조회 (토큰 필요)
curl http://localhost:8082/dong-code/sido \
  -H "access-token: <JWT_TOKEN>"

# 게시판 서비스 — 게시글 목록
curl http://localhost:8083/board \
  -H "access-token: <JWT_TOKEN>"

# 뉴스 서비스 — 부동산 뉴스
curl http://localhost:8085/news/all
```

### Step 1-5. 전체 중지

```bash
docker compose down
```

---

## ☸️ Part 2: Kubernetes(k8s) 배포 — 단계별 Lab

> 이 Lab에서는 **minikube**를 기준으로 설명합니다. 다른 클러스터(k3s, Docker Desktop, kubeadm 등)도 동일한 `kubectl` 명령이 적용됩니다.

---

### Lab 0. Kubernetes 클러스터 준비

#### minikube 시작

```bash
# minikube 시작 (메모리 4GB 권장)
minikube start --memory=4096 --cpus=2

# 클러스터 상태 확인
kubectl cluster-info
kubectl get nodes
```

예상 출력:
```
NAME       STATUS   ROLES           AGE   VERSION
minikube   Ready    control-plane   1m    v1.29.0
```

#### minikube Docker 환경 연결 (중요!)

minikube는 자체 Docker 데몬을 가지고 있습니다. 로컬에서 빌드한 이미지를 minikube Pod에서 사용하려면 **minikube의 Docker 환경으로 전환**해야 합니다.

```bash
# minikube Docker 환경을 현재 쉘에 적용
eval $(minikube docker-env)

# 확인 (DOCKER_HOST가 minikube IP를 가리켜야 함)
echo $DOCKER_HOST
```

> ⚠️ 이 명령은 **현재 터미널 세션에만** 적용됩니다. 새 터미널을 열면 다시 실행해야 합니다.

---

### Lab 1. Docker 이미지 빌드 (minikube 환경에서)

각 서비스를 Docker 이미지로 빌드합니다. `imagePullPolicy: Never`이므로 반드시 minikube Docker 환경에서 빌드해야 합니다.

```bash
# 1) minikube Docker 환경 연결 확인
eval $(minikube docker-env)

# 2) 각 서비스 이미지 빌드
docker build -t whereis/user-service:latest  ./user-service/
docker build -t whereis/apt-service:latest   ./apt-service/
docker build -t whereis/board-service:latest ./board-service/
docker build -t whereis/admin-service:latest ./admin-service/
docker build -t whereis/news-service:latest  ./news-service/

# 3) 빌드된 이미지 목록 확인
docker images | grep whereis
```

예상 출력:
```
REPOSITORY               TAG       IMAGE ID       CREATED         SIZE
whereis/user-service     latest    abc123...      2 min ago       280MB
whereis/apt-service      latest    def456...      3 min ago       285MB
whereis/board-service    latest    ghi789...      4 min ago       280MB
whereis/admin-service    latest    jkl012...      5 min ago       280MB
whereis/news-service     latest    mno345...      6 min ago       250MB
```

> 💡 **이미지 빌드 시간 절약 팁**: Dockerfile의 멀티스테이지 빌드로 인해 처음 빌드는 Maven 의존성 다운로드로 5~10분 걸릴 수 있습니다. 이후 빌드는 Docker 캐시로 빠르게 완료됩니다.

#### Dockerfile 구조 설명

각 서비스의 `Dockerfile`은 **멀티스테이지 빌드** 방식을 사용합니다.

```dockerfile
# Stage 1: 빌드 스테이지 — Maven으로 JAR 파일 생성
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn -q package -DskipTests

# Stage 2: 실행 스테이지 — JRE만 포함한 경량 이미지
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/<service>-0.0.1-SNAPSHOT.jar app.jar
EXPOSE <PORT>
ENTRYPOINT ["java", "-jar", "app.jar"]
```

| 단계 | 역할 | 결과 |
|---|---|---|
| `AS build` | JDK + Maven으로 소스 컴파일 및 패키징 | `*.jar` 생성 |
| 최종 스테이지 | JRE만 포함한 경량 실행 환경 | 실행 가능한 컨테이너 이미지 |

---

### Lab 2. MariaDB 배포

#### Step 2-1. MariaDB Secret 및 PVC 생성

```bash
kubectl apply -f k8s/mariadb.yaml
```

이 명령 하나로 다음 리소스가 모두 생성됩니다:
- `Secret` (mariadb-secret) — DB 비밀번호를 Base64로 안전하게 저장
- `ConfigMap` (mariadb-config) — DB명, 타임존 등 일반 설정값
- `PersistentVolumeClaim` (mariadb-pvc) — 데이터 영속 저장용 5GB 볼륨
- `Deployment` (mariadb) — MariaDB Pod 정의
- `Service` (mariadb, ClusterIP) — 클러스터 내부에서 `mariadb:3306`으로 접근 가능

#### Step 2-2. DB 초기화 SQL ConfigMap 생성

```bash
# db.sql 파일을 ConfigMap으로 등록 (Pod 기동 시 자동 실행됨)
kubectl create configmap mariadb-init-sql \
  --from-file=01-db.sql=db.sql
```

#### Step 2-3. MariaDB Pod 상태 확인

```bash
# Pod가 Running 상태가 될 때까지 대기
kubectl get pods -l app=mariadb -w
```

예상 출력 (약 30초~1분 소요):
```
NAME                       READY   STATUS              RESTARTS   AGE
mariadb-7d9f8b6c4d-xkv2p   0/1     ContainerCreating   0          10s
mariadb-7d9f8b6c4d-xkv2p   0/1     Running             0          20s
mariadb-7d9f8b6c4d-xkv2p   1/1     Running             0          40s
```

> `Ctrl+C`로 watch 모드 종료 후 다음 단계로 진행합니다.

#### Step 2-4. DB 연결 확인 (선택)

```bash
# MariaDB Pod 내부에서 DB 접속 확인
kubectl exec -it $(kubectl get pod -l app=mariadb -o jsonpath='{.items[0].metadata.name}') \
  -- mariadb -uroot -p123456 -e "show databases;"
```

---

### Lab 3. 각 서비스 Pod 배포

#### Step 3-1. 전체 서비스 한 번에 배포

```bash
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/apt-service.yaml
kubectl apply -f k8s/board-service.yaml
kubectl apply -f k8s/admin-service.yaml
kubectl apply -f k8s/news-service.yaml
```

각 YAML 파일에는 **Deployment**와 **NodePort Service** 두 리소스가 포함됩니다.

**Deployment가 하는 일:**
1. 지정한 Docker 이미지(`whereis/<service>:latest`)로 컨테이너 생성
2. 환경변수(`SERVER_PORT`, `SPRING_DATASOURCE_URL` 등) 주입
3. `readinessProbe`로 애플리케이션이 준비될 때까지 트래픽 차단

**NodePort Service가 하는 일:**
1. 특정 NodePort(예: 30081)로 들어오는 트래픽을 해당 Pod의 컨테이너 포트로 포워딩
2. 클러스터 외부에서 `<Node_IP>:<NodePort>`로 직접 접근 가능

#### Step 3-2. 전체 Pod 상태 확인

```bash
kubectl get pods
```

예상 출력 (약 1~2분 후 모두 Running):
```
NAME                              READY   STATUS    RESTARTS   AGE
mariadb-7d9f8b6c4d-xkv2p          1/1     Running   0          5m
user-service-5f4d8b9c7-abcde      1/1     Running   0          2m
apt-service-6b7c9d4e8-fghij       1/1     Running   0          2m
board-service-9e2f1a3b5-klmno     1/1     Running   0          2m
admin-service-3d8e7c5f1-pqrst     1/1     Running   0          2m
news-service-2a4b6c8d0-uvwxy      1/1     Running   0          2m
```

#### Step 3-3. Service(NodePort) 확인

```bash
kubectl get services
```

예상 출력:
```
NAME            TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
kubernetes      ClusterIP   10.96.0.1       <none>        443/TCP          10m
mariadb         ClusterIP   10.96.12.34     <none>        3306/TCP         5m
user-service    NodePort    10.96.23.45     <none>        8081:30081/TCP   2m
apt-service     NodePort    10.96.34.56     <none>        8082:30082/TCP   2m
board-service   NodePort    10.96.45.67     <none>        8083:30083/TCP   2m
admin-service   NodePort    10.96.56.78     <none>        8084:30084/TCP   2m
news-service    NodePort    10.96.67.89     <none>        8085:30085/TCP   2m
```

---

### Lab 4. NodePort로 API 접근

#### minikube IP 확인

```bash
minikube ip
# 예: 192.168.49.2
```

이후 모든 API 호출은 `http://<MINIKUBE_IP>:<NODE_PORT>/` 형식으로 사용합니다.

```bash
# 편의를 위해 환경변수로 설정
export NODE_IP=$(minikube ip)
echo "Minikube IP: $NODE_IP"
```

#### NodePort 동작 원리

```
외부 클라이언트
       │  http://192.168.49.2:30081/users/login
       ▼
  ┌─────────────────────────────────┐
  │  Kubernetes Node (minikube VM)  │
  │                                 │
  │  NodePort: 30081                │
  │        │ iptables 규칙으로       │
  │        ▼ 자동 포워딩             │
  │  ┌─────────────────────────┐    │
  │  │ user-service Pod        │    │
  │  │  ContainerPort: 8081    │    │
  │  │  Spring Boot 앱 실행 중 │    │
  │  └─────────────────────────┘    │
  └─────────────────────────────────┘
```

---

### Lab 5. 서비스별 API 테스트

#### 🧑 user-service (NodePort: 30081)

```bash
# [1] 회원가입
curl -X POST http://$NODE_IP:30081/users/join \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "admin",
    "userName": "관리자",
    "userPwd": "admin1234",
    "emailId": "admin",
    "emailDomain": "ssafy.com"
  }'
# 응답: "success" (HTTP 201)

# [2] 로그인 → JWT 토큰 발급
TOKEN=$(curl -s -X POST http://$NODE_IP:30081/users/login \
  -H "Content-Type: application/json" \
  -d '{"id":"admin","pwd":"admin1234"}' \
  | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

echo "발급된 토큰: $TOKEN"

# [3] 회원 정보 조회
curl http://$NODE_IP:30081/users/confirm/admin \
  -H "access-token: $TOKEN"
```

#### 🏠 apt-service (NodePort: 30082)

```bash
# [1] 시도 목록 조회
curl http://$NODE_IP:30082/dong-code/sido \
  -H "access-token: $TOKEN"

# [2] 특정 동 아파트 조회 (regcode: 법정동코드)
curl "http://$NODE_IP:30082/apts?regcode=1168010700&userId=admin" \
  -H "access-token: $TOKEN"

# [3] 관심 아파트 목록 조회
curl http://$NODE_IP:30082/apts/ck/admin \
  -H "access-token: $TOKEN"
```

#### 📋 board-service (NodePort: 30083)

```bash
# [1] 게시글 목록 조회
curl http://$NODE_IP:30083/board \
  -H "access-token: $TOKEN"

# [2] 게시글 작성
curl -X POST http://$NODE_IP:30083/board/write \
  -H "Content-Type: application/json" \
  -H "access-token: $TOKEN" \
  -d '{
    "userId": "admin",
    "subject": "테스트 게시글",
    "content": "MSA 배포 완료!",
    "bullet": "일반"
  }'
# 응답: "success" (HTTP 201)

# [3] 게시글 검색
curl "http://$NODE_IP:30083/board/search/테스트" \
  -H "access-token: $TOKEN"
```

#### 👮 admin-service (NodePort: 30084)

```bash
# [1] 전체 회원 목록 조회
curl http://$NODE_IP:30084/admin \
  -H "access-token: $TOKEN"

# [2] 회원 권한 변경 (T: 관리자, F: 일반)
curl -X PUT http://$NODE_IP:30084/admin/admin/T \
  -H "access-token: $TOKEN"
```

#### 📰 news-service (NodePort: 30085)

```bash
# 부동산 뉴스 조회 (인증 불필요)
curl http://$NODE_IP:30085/news/all
```

---

### Lab 6. Swagger UI로 API 문서 확인

각 서비스는 SpringDoc OpenAPI(Swagger UI)를 제공합니다.

브라우저에서 아래 URL에 접속합니다:

| 서비스 | Swagger UI URL |
|---|---|
| user-service | `http://<MINIKUBE_IP>:30081/swagger-ui/index.html` |
| apt-service | `http://<MINIKUBE_IP>:30082/swagger-ui/index.html` |
| board-service | `http://<MINIKUBE_IP>:30083/swagger-ui/index.html` |
| admin-service | `http://<MINIKUBE_IP>:30084/swagger-ui/index.html` |
| news-service | `http://<MINIKUBE_IP>:30085/swagger-ui/index.html` |

> minikube를 사용하는 경우 `minikube service user-service --url` 명령으로 접근 가능한 URL을 확인할 수도 있습니다.

---

### Lab 7. Pod 로그 및 디버깅

#### 특정 서비스 로그 확인

```bash
# user-service 로그
kubectl logs -l app=user-service --tail=50 -f

# apt-service 로그
kubectl logs -l app=apt-service --tail=50 -f

# board-service 로그
kubectl logs -l app=board-service --tail=50 -f

# 특정 Pod 이름으로 조회
kubectl logs <POD_NAME> -f
```

#### Pod 내부 접속 (디버깅)

```bash
kubectl exec -it $(kubectl get pod -l app=user-service -o jsonpath='{.items[0].metadata.name}') -- sh
```

#### 이벤트 확인 (Pod 기동 실패 시)

```bash
kubectl describe pod -l app=user-service
kubectl get events --sort-by='.metadata.creationTimestamp'
```

---

### Lab 8. 스케일 아웃 (수평 확장)

마이크로서비스의 장점인 독립적인 스케일 아웃을 실습합니다.

```bash
# board-service를 3개 복제본으로 확장
kubectl scale deployment board-service --replicas=3

# 확장 확인
kubectl get pods -l app=board-service
```

예상 출력:
```
NAME                             READY   STATUS    RESTARTS   AGE
board-service-9e2f1a3b5-klmno    1/1     Running   0          10m
board-service-9e2f1a3b5-pqrst    1/1     Running   0          30s
board-service-9e2f1a3b5-uvwxy    1/1     Running   0          30s
```

```bash
# 다시 1개로 축소
kubectl scale deployment board-service --replicas=1
```

---

### Lab 9. 롤링 업데이트

소스코드 변경 후 무중단 배포를 실습합니다.

```bash
# 1. 코드 수정 후 새 이미지 빌드 (minikube docker-env에서)
eval $(minikube docker-env)
docker build -t whereis/user-service:v2 ./user-service/

# 2. Deployment 이미지 업데이트
kubectl set image deployment/user-service \
  user-service=whereis/user-service:v2

# 3. 롤아웃 상태 확인
kubectl rollout status deployment/user-service

# 4. 문제 발생 시 이전 버전으로 롤백
kubectl rollout undo deployment/user-service
```

---

### Lab 10. 리소스 정리

```bash
# 서비스 전체 삭제
kubectl delete -f k8s/

# minikube 중지
minikube stop

# minikube 완전 삭제 (선택)
minikube delete
```

---

## 🔁 전체 배포 흐름 요약

```
[소스코드]
    │
    ▼ mvn package (Dockerfile 내 멀티스테이지 빌드)
[JAR 파일]
    │
    ▼ docker build
[Docker 이미지]  ← minikube docker-env에서 빌드
    │
    ▼ kubectl apply -f k8s/<service>.yaml
[Kubernetes Deployment]
    │
    ▼ ReplicaSet이 Pod 생성
[Pod 1개 이상]
    │
    ▼ NodePort Service가 외부 트래픽을 Pod로 라우팅
[외부 클라이언트] → http://<NODE_IP>:<NODE_PORT>/...
```

---

## 🚨 트러블슈팅 FAQ

### Q1. Pod가 `ImagePullBackOff` 상태일 때

```bash
# 원인: minikube 외부 Docker Hub에서 이미지를 찾으려 함
# 해결: minikube Docker 환경에서 이미지를 다시 빌드
eval $(minikube docker-env)
docker build -t whereis/<service-name>:latest ./<service-name>/
```

### Q2. Pod가 `CrashLoopBackOff` 상태일 때

```bash
# 로그 확인
kubectl logs <POD_NAME> --previous

# 주요 원인:
# - DB 연결 실패: mariadb Pod가 Ready 상태인지 확인
kubectl get pods -l app=mariadb
# - 환경변수 오류: Secret 이름/키 확인
kubectl describe secret mariadb-secret
```

### Q3. DB 초기화가 안 됐을 때

```bash
# ConfigMap 재생성
kubectl delete configmap mariadb-init-sql
kubectl create configmap mariadb-init-sql --from-file=01-db.sql=db.sql

# MariaDB Pod 재시작 (PVC 데이터 초기화 필요 시)
kubectl delete pod -l app=mariadb
# ⚠️ PVC가 남아있으면 init script가 재실행되지 않음
# 완전 초기화: kubectl delete pvc mariadb-pvc 후 재배포
```

### Q4. minikube IP로 접속이 안 될 때

```bash
# minikube service 터널 사용
minikube service user-service

# 또는 포트 포워딩
kubectl port-forward service/user-service 8081:8081
# → http://localhost:8081 로 접근 가능
```

### Q5. 로컬 PC에서 Docker Compose 빌드 후 minikube에 이미지 로드

```bash
# 로컬에서 이미지 빌드 후 minikube에 직접 로드
docker build -t whereis/user-service:latest ./user-service/
minikube image load whereis/user-service:latest
```

---

## 📊 서비스별 API 엔드포인트 요약

### user-service (:30081)

| Method | Path | 설명 | 인증 |
|---|---|---|---|
| POST | `/users/join` | 회원가입 | ❌ |
| POST | `/users/login` | 로그인 (JWT 발급) | ❌ |
| POST | `/users/find-id` | 아이디 찾기 | ❌ |
| POST | `/users/find-pwd` | 비밀번호 찾기 | ❌ |
| GET | `/users/confirm/{id}` | 회원정보 조회 | ✅ |
| PUT | `/users/update` | 회원정보 수정 | ✅ |
| DELETE | `/users/{id}` | 회원 탈퇴 | ✅ |
| GET | `/users/logout` | 로그아웃 | ✅ |

### apt-service (:30082)

| Method | Path | 설명 | 인증 |
|---|---|---|---|
| GET | `/dong-code/sido` | 시도 목록 | ✅ |
| GET | `/dong-code/gugun/{sido-code}` | 구군 목록 | ✅ |
| GET | `/dong-code/dong/{regcode}` | 동 목록 | ✅ |
| GET | `/apts?regcode=&userId=` | 동 단위 아파트 조회 | ✅ |
| GET | `/apts/guguns?regcode=&userId=` | 구군 단위 아파트 조회 | ✅ |
| GET | `/apts/ck/{id}` | 관심 아파트 목록 | ✅ |
| POST | `/apts/ck/{id}` | 관심 아파트 저장 | ✅ |
| DELETE | `/apts/ck/{id}/{aptCode}` | 관심 아파트 삭제 | ✅ |

### board-service (:30083)

| Method | Path | 설명 | 인증 |
|---|---|---|---|
| GET | `/board` | 게시글 목록 | ✅ |
| GET | `/board/{articleNo}` | 게시글 상세 | ✅ |
| GET | `/board/search/{word}` | 게시글 검색 | ✅ |
| POST | `/board/write` | 게시글 작성 | ✅ |
| PUT | `/board/modify/{articleNo}` | 게시글 수정 | ✅ |
| DELETE | `/board/delete/{articleNo}` | 게시글 삭제 | ✅ |

### admin-service (:30084)

| Method | Path | 설명 | 인증 |
|---|---|---|---|
| GET | `/admin` | 전체 회원 목록 | ✅ |
| DELETE | `/admin/{userId}` | 회원 삭제 | ✅ |
| PUT | `/admin/{userId}/{auth}` | 권한 변경 (T/F) | ✅ |

### news-service (:30085)

| Method | Path | 설명 | 인증 |
|---|---|---|---|
| GET | `/news/all` | 부동산 뉴스 목록 | ❌ |

> ✅ = `access-token` 헤더에 JWT 토큰 필요, ❌ = 인증 불필요

---

## 🔐 인증 흐름

모든 인증이 필요한 API는 로그인 후 발급된 JWT 토큰을 `access-token` 헤더에 포함해야 합니다.

```bash
# 1. 로그인으로 토큰 발급
TOKEN=$(curl -s -X POST http://$NODE_IP:30081/users/login \
  -H "Content-Type: application/json" \
  -d '{"id":"admin","pwd":"admin1234"}' | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

# 2. 이후 모든 요청에 헤더 추가
curl http://$NODE_IP:30083/board -H "access-token: $TOKEN"
```

---

*이 가이드는 WhereIsMyHome 프로젝트의 MSA 전환 및 Kubernetes 배포 실습을 위해 작성되었습니다.*

