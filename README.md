
```markdown
# 스프링부트 V2 - Persistence Context 학습 블로그

> Spring Boot와 JPA의 **영속성 컨텍스트(Persistence Context)** 핵심 개념을 학습하는 익명 블로그 프로젝트

## 🎯 V2 핵심 학습 목표

- **영속성 컨텍스트** 완전 이해 및 활용
- **도메인 중심 아키텍처** 설계 경험
- **JPQL** 기반 객체지향 쿼리 작성
- **JPA 생명주기** 및 **Dirty Checking** 마스터

## 🏗️ 패키지 구조 혁신

### V1 (계층형) → V2 (도메인형)

com.tenco.blog
├── board/                    # Board 도메인의 모든 것이 한 곳에
│   ├── Board                 # 엔티티
│   ├── BoardController       # 컨트롤러
│   ├── BoardPersistRepository # 리포지토리
│   └── BoardRequest          # DTO
├── user/                     # User 도메인의 모든 것이 한 곳에
│   ├── User
│   ├── UserController
│   ├── UserRepository
│   └── UserRequest
└── utils/                    # 공통 기능
    └── MyDateUtil


**장점**: 도메인별 응집도 향상, 실무 지향적 구조

## 🔄 영속성 컨텍스트 핵심 기능

### 1. **엔티티 저장 (Persist)**
```java
@Transactional
public Board save(Board board) {
    em.persist(board);  // 비영속 → 영속 상태 변경
    return board;       // 트랜잭션 커밋 시 INSERT 실행
}
```

### 2. **1차 캐시 활용 조회**
```java
public Board findById(Long id) {
    return em.find(Board.class, id);  // 1차 캐시 우선 확인
}
```

### 3. **Dirty Checking 수정**
```java
@Transactional
public void updateById(Long id, BoardRequest.UpdateDTO reqDTO) {
    Board board = em.find(Board.class, id);  // 영속 상태로 조회
    board.update(reqDTO);                     // 값 변경만
    // persist() 불필요! 자동 UPDATE 쿼리 생성
}
```

### 4. **JPQL 목록 조회**
```java
public List<Board> findAll() {
    String jpql = "SELECT b FROM Board b ORDER BY b.createdAt DESC";
    return em.createQuery(jpql, Board.class).getResultList();
}
```

## 🚀 기술 스택

- **Java**: 21
- **Spring Boot**: 3.3.12
- **JPA/Hibernate**: 영속성 컨텍스트 활용
- **H2 Database**: 인메모리 개발 DB
- **Mustache**: 템플릿 엔진
- **Lombok**: 보일러플레이트 코드 제거

## ⚡ 실행 방법

```bash
# 1. 프로젝트 클론
git clone [repository-url]
cd spring-boot-blog-v2

# 2. 애플리케이션 실행
./gradlew bootRun

# 3. 접속 확인
http://localhost:8080        # 메인 페이지
http://localhost:8080/h2-console  # H2 콘솔
```

## 📋 주요 API

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/` | 게시글 목록 (JPQL 활용) |
| GET | `/board/save-form` | 게시글 작성 폼 |
| POST | `/board/save` | 게시글 저장 (Persist) |
| GET | `/board/{id}` | 게시글 상세 (1차 캐시) |
| GET | `/board/{id}/update-form` | 게시글 수정 폼 |
| POST | `/board/{id}/update` | 게시글 수정 (Dirty Checking) |
| POST | `/board/{id}/delete` | 게시글 삭제 (Remove) |

## 🎓 V1 → V2 주요 변화

### 아키텍처
- **계층형** → **도메인형** 패키지 구조
- **Native SQL** → **JPQL** 객체지향 쿼리

### JPA 활용도
- **직접 SQL 작성** → **영속성 컨텍스트** 활용
- **수동 UPDATE** → **Dirty Checking** 자동 변경 감지
- **매번 DB 조회** → **1차 캐시** 성능 최적화

### 개발 생산성
- **반복적 SQL** → **EntityManager** 추상화
- **트랜잭션 수동 관리** → **@Transactional** 자동 관리


