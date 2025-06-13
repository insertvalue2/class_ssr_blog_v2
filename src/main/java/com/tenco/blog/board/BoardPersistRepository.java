package com.tenco.blog.board;

/**
 * Persistence Context 활용한 Repository 만들기
 * Repository란
 * "저장소", "보관소", "창고"를 의미합니다.
 * 소프트웨어에서는 데이터를 저장하고 관리하는 곳을
 * 추상화한 개념입니다.
 */


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class BoardPersistRepository {

    // EntityManager: JPA의 핵심 인터페이스
    // 영속성 컨텍스트를 관리하고 엔티티의 생명주기를 제어
    private final EntityManager em;

    // 게시글 저장: Persistence Context를 활용한 엔티티 영속화
    @Transactional
    public Board save(Board board) {
        // 1. 매개변수로 받은 board는 비영속(Transient) 상태
        //    - 아직 영속성 컨텍스트에 관리되지 않는 상태
        //    - 데이터베이스와 연관 없는 순수 Java 객체

        // 2. em.persist(board): 엔티티를 영속성 컨텍스트에 저장
        //    - board 객체가 영속(Managed) 상태로 변경됨
        //    - 영속성 컨텍스트가 엔티티를 관리 시작
        //    - 아직 실제 INSERT 쿼리는 실행되지 않음 (지연 쓰기)
        em.persist(board);

        // 3. 트랜잭션 커밋 시점에 실제 INSERT 쿼리 실행
        //    - @Transactional 메서드 종료 시 자동 커밋
        //    - 이때 영속성 컨텍스트의 변경사항이 DB에 반영됨
        //    - board 객체의 id 필드에 자동 생성된 값이 설정됨

        // 4. 영속 상태의 board 객체 반환
        //    - 이제 board는 영속성 컨텍스트에서 관리되는 엔티티
        //    - 자동으로 생성된 id 값을 포함
        return board;
    }
}