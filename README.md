# 📚 BookDiary

> 📖 독서 기록 및 관리 웹 애플리케이션  
> 👥 3팀 협업 프로젝트

---

## 📌 프로젝트 소개

BookDiary는 사용자의 독서 기록을 관리하고,  
책 정보 및 활동을 기록할 수 있는 웹 애플리케이션입니다.

---

## ⚙️ 기술 스택

- **Backend**: Spring Boot, Spring Security, JPA
- **Frontend**: Thymeleaf
- **Database**: MySQL
- **Infra**: (추후 추가)
- **Authentication**: JWT

---
---

# BookDiary 실행 가이드

## 사전 요구사항

- Docker
- Docker Compose
- Git

---

## 1. 프로젝트 클론

```bash
git clone https://github.com/CLD-05/team03_BookDiary.git
cd team03_BookDiary
```

---

## 2. 환경 변수 설정

프로젝트 루트(`/opt/bookdiary`)에 `.env` 파일을 생성합니다.

```bash
cat > .env << 'EOF'
DB_NAME=bookdiary
DB_USER=bookdiary
DB_PASSWORD=1234
DB_ROOT_PASSWORD=root1234
JWT_SECRET=bookdiary-secret-key-bookdiary-secret-key
JWT_EXPIRATION=86400000
KAKAO_REST_KEY=your_kakao_api_key
GEMINI_API_KEY=your_gemini_api_key
EOF
```

---

## 3. application.properties 생성

```bash
cat > BookDiary/src/main/resources/application.properties << 'EOF'
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://db:3306/bookdiary?serverTimezone=Asia/Seoul
spring.datasource.username=bookdiary
spring.datasource.password=1234

spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl

spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false

spring.batch.job.enabled=false

spring.ai.google.genai.chat.enabled=true
spring.ai.google.genai.api-key=your_gemini_api_key
spring.ai.google.genai.chat.options.model=gemini-2.5-flash

jwt.secret=bookdiary-secret-key-bookdiary-secret-key
jwt.expiration=86400000

kakao.api.rest-key=your_kakao_api_key
kakao.book-search.url=https://dapi.kakao.com/v3/search/book
EOF
```

---

## 4. Docker Hub 이미지 선택

팀원 중 한 명의 이미지를 선택하여 사용합니다.

| 팀원 | Docker Hub 이미지 | 링크 |
|------|-------------------|------|
| 김노을 | `commcencer/bookdiary:1.0` | [바로가기](https://hub.docker.com/repository/docker/commcencer/bookdiary/general) |
| 김시현 | `shkim5971/bookdiary:1.0` | [바로가기](https://hub.docker.com/repository/docker/shkim5971/bookdiary/general) |
| 신솔미 | `solmi2449/bookdiary:1.0` | [바로가기](https://hub.docker.com/repository/docker/solmi2449/bookdiary/general) |
| 김원호 | `dnjsgh/bookdiary:1.0` | [바로가기](https://hub.docker.com/repository/docker/dnjsgh/bookdiary/general) |
| 이유은 | `edbdmss/bookdiary:1.0` | [바로가기](https://hub.docker.com/repository/docker/edbdmss/bookdiary/general) |

`docker-compose.yml`의 app 서비스를 아래와 같이 수정합니다.

```yaml
app:
  image: commcencer/bookdiary:1.0  # 원하는 이미지로 변경
  container_name: bookdiary-app
```

---

## 5. 실행

### Docker Hub 이미지로 실행 (권장)

빌드 없이 바로 실행합니다.

```bash
cd /opt/bookdiary
docker compose up -d
```

### 직접 빌드 후 실행

소스코드를 직접 빌드하여 실행합니다.

```bash
cd /opt/bookdiary
docker compose up -d --build
```

---

## 6. 실행 흐름

```
git clone
    ↓
.env 파일 생성
    ↓
application.properties 생성
    ↓
docker-compose.yml에서 이미지 선택
    ↓
docker compose up -d
    ↓
DB 컨테이너 기동 (healthcheck 대기)
    ↓
App 컨테이너 기동
    ↓
http://localhost:8080 접속
```

---

## 7. 상태 확인

```bash
# 컨테이너 상태 확인
docker compose ps

# 앱 로그 확인
docker compose logs -f app

# DB 로그 확인
docker compose logs -f db

# DB 데이터 확인
docker exec -it bookdiary-db mysql -u bookdiary -p1234 -e "SELECT * FROM bookdiary.tbUser;"
```

---

## 8. 서비스 종료

```bash
# 컨테이너만 종료
docker compose down

# 컨테이너 + 볼륨 전체 삭제 (데이터 초기화)
docker compose down -v
```

---

## 9. 디렉토리 구조

```
/opt/bookdiary/
├── docker-compose.yml
├── .env
├── data/
│   ├── db-conf/
│   │   └── my.cnf
│   ├── db-init/
│   │   └── init.sql
│   └── mysql/
├── logs/
└── BookDiary/
    ├── Dockerfile
    ├── src/
    ├── pom.xml
    └── mvnw
```

---

## 10. 초기 계정

| 아이디 | 비밀번호 | 권한 |
|--------|----------|------|
| test   | 1234     | 일반 사용자 |
| admin  | 1234     | 관리자 |

---
---

## 🌿 브랜치 전략

본 프로젝트는 **Git Flow 기반 커스텀 전략**을 사용합니다.

### 🔹 브랜치 네이밍 규칙

| 타입 | 형식 |
|------|------|
| 개발 | `DEV/이름_날짜/작업내용` |
| 수정 | `FIX/이름_날짜/작업내용` |
| 빌드 | `BUILD/버전_날짜` |

### 📌 예시

```bash
DEV/홍길동_20251101/login-api
FIX/홍길동_20251101/null-error
BUILD/1.0.0_20251101
```

# 🔀 Pull Request 규칙
PR 작성 시 아래 템플릿을 반드시 작성해주세요.

## 🔍 개요
- 작업 내용에 대한 간단한 설명

## 🧪 변경 내용
- [ ] 기능 추가
- [ ] 버그 수정
- [ ] 리팩토링
- [ ] 기타

## ✅ 테스트 방법
- 테스트 방법 또는 확인 방법 작성

## 📸 스크린샷 (선택)
- UI 변경 시 첨부
