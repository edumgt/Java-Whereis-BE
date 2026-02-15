# WSL(root) 환경에서 GitHub SSH + Docker Desktop 연동 + run-local.sh 실행까지 (전체 정리)

> 환경: Windows + WSL2(Ubuntu), **root 사용자로 작업**, GitHub repo: `edumgt/Java-Whereis-BE`

---

## 0. 문제 흐름 요약

### (1) Git clone (HTTPS)
- 증상:
  - `Password authentication is not supported for Git operations.`
- 원인:
  - GitHub는 **HTTPS 비밀번호 인증을 종료** (토큰/PAT 또는 SSH 필요)

### (2) Git clone (SSH)
- 증상:
  - `Permission denied (publickey).`
- 원인:
  - SSH 키가 없거나, ssh-agent 미등록, GitHub에 공개키 미등록

### (3) run-local.sh 실행 시 Docker
- 증상:
  - `Cannot connect to the Docker daemon at unix:///var/run/docker.sock`
  - `/var/run/docker.sock` 없음
- 원인:
  - Docker CLI는 설치되어 있으나 **Docker daemon/소켓이 Ubuntu에 붙지 않음**
  - Docker Desktop의 `docker-desktop` WSL distro가 **Stopped** 상태였음

---

## 1) GitHub SSH 인증 설정 (root 기준)

### 1-1. SSH 키 생성 (이미 수행됨)
```bash
ssh-keygen -t ed25519 -C "edumgt@github"
```

생성 파일:
- 개인키: `/root/.ssh/id_ed25519`
- 공개키: `/root/.ssh/id_ed25519.pub`

### 1-2. ssh-agent 실행 및 키 등록 (이미 수행됨)
```bash
eval "$(ssh-agent -s)"
ssh-add /root/.ssh/id_ed25519
```

등록 확인:
```bash
ssh-add -l
```

### 1-3. GitHub에 공개키 등록 (필수)
공개키 확인:
```bash
cat /root/.ssh/id_ed25519.pub
```

GitHub 웹에서:
- Settings → SSH and GPG keys → New SSH key
  - Title: 예) `WSL-root-CLQV18N`
  - Key: `id_ed25519.pub` 출력 내용 붙여넣기

### 1-4. SSH 인증 테스트
```bash
ssh -T git@github.com
```

정상 예시:
- `Hi edumgt! You've successfully authenticated, but GitHub does not provide shell access.`

### 1-5. SSH로 clone
```bash
git clone git@github.com:edumgt/Java-Whereis-BE.git
```

---

## 2) Maven(mvn) 오류 해결

### 2-1. mvnw(Wrapper) 유무 확인
```bash
ls -al mvnw .mvn 2>/dev/null || true
```

- 결과: `mvnw`가 없었음 → Maven 설치 필요

### 2-2. Maven 설치
```bash
apt-get update
apt-get install -y maven
mvn -v
```

> 추가로 Java 버전도 확인 권장:
```bash
java -version
# 필요 시 (예: JDK 17)
apt-get install -y openjdk-17-jdk
```

---

## 3) Docker Desktop ↔ WSL2(Ubuntu) 연동으로 docker.sock 생성

### 3-1. 증상
`bash scripts/run-local.sh` 실행 시:
- `Cannot connect to the Docker daemon ... /var/run/docker.sock`
- `/var/run/docker.sock` 파일 자체가 없었음

확인:
```bash
docker version
ls -al /var/run/docker.sock || true
```
- Client만 출력되고 Server가 없으며,
- `No such file or directory` 발생

### 3-2. 핵심 원인
PowerShell에서 확인:
```powershell
wsl -l -v
```

- `docker-desktop` distro가 **Stopped**면 Ubuntu에 소켓이 안 붙음

### 3-3. 해결: docker-desktop distro를 Running 상태로 만들기 (PowerShell)
```powershell
wsl -d docker-desktop -u root -- echo "docker-desktop started"
wsl -l -v
```

정상 예시:
- `docker-desktop    Running`

### 3-4. Ubuntu에서 소켓 생성 확인 (성공 상태)
```bash
ls -al /mnt/wsl || true
ls -al /mnt/wsl/docker-desktop 2>/dev/null || echo "no /mnt/wsl/docker-desktop"
ls -al /var/run/docker.sock 2>/dev/null || echo "no /var/run/docker.sock"
```

성공 시 예:
- `/mnt/wsl/docker-desktop` 존재
- `/var/run/docker.sock` 존재 (socket 파일)

> 실제 성공 출력에서 확인된 상태:
- `/mnt/wsl/docker-desktop/shared-sockets` 존재
- `srw-rw---- 1 root docker ... /var/run/docker.sock` 생성됨

### 3-5. Docker 동작 확인
```bash
docker version
docker ps
```

`docker version`에 **Server** 정보가 같이 나오면 정상입니다.

---

## 4) run-local.sh 재실행

Docker 소켓이 정상인 상태에서:
```bash
cd /home/Java-Whereis-BE
bash scripts/run-local.sh
```

---

## 5) 트러블슈팅 체크리스트

### 5-1. docker.sock이 또 없어지면?
- PowerShell:
```powershell
wsl -l -v
```
- `docker-desktop`이 Running인지 확인
- 필요 시:
```powershell
wsl -d docker-desktop -u root -- echo "docker-desktop started"
```

### 5-2. 일반 사용자로 Docker 권한 이슈(permission denied)가 나면?
(root가 아닌 사용자일 때)
```bash
usermod -aG docker <사용자명>
# 재로그인 또는 wsl --shutdown 후 재진입
```

### 5-3. 다음 단계에서 자주 막히는 지점
- `mariadb:11.4` 이미지 pull
- 포트 충돌(3306/8080 등)
- DB 초기화 SQL / 환경변수
- Java 버전(특히 17/21 요구)
- `.env` 또는 `application.yml` 설정

---

## 빠른 명령 모음

### 상태 점검 1줄 세트
```bash
whoami
docker version
ls -al /var/run/docker.sock || true
mvn -v || true
java -version || true
```

---

끝.
