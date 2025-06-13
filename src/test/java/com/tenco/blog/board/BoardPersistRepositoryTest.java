package com.tenco.blog.board;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

// BoardPersistRepository를 테스트 컨텍스트에 추가
@Import(BoardPersistRepository.class)
@DataJpaTest
public class BoardPersistRepositoryTest {

    @Autowired
    private BoardPersistRepository boardPersistRepository;

    @Test
    public void save_test(){
        // given: 테스트할 게시글 데이터 준비
        // new Board() 생성자를 통해 비영속 엔티티 생성
        Board board = new Board("제목5", "내용5", "ssar");

        // 저장 전 상태 확인: id는 null이어야 함
        Assertions.assertThat(board.getId()).isNull();
        System.out.println("저장 전 board : " + board);

        // when: 영속성 컨텍스트를 통한 엔티티 저장
        Board savedBoard = boardPersistRepository.save(board);

        // then: 저장 결과 검증
        // 1. 저장 후 자동 생성된 ID 확인
        Assertions.assertThat(savedBoard.getId()).isNotNull();
        Assertions.assertThat(savedBoard.getId()).isGreaterThan(0);

        // 2. 입력한 데이터가 올바르게 저장되었는지 확인
        Assertions.assertThat(savedBoard.getTitle()).isEqualTo("제목5");
        Assertions.assertThat(savedBoard.getContent()).isEqualTo("내용5");
        Assertions.assertThat(savedBoard.getUsername()).isEqualTo("ssar");

        // 3. 자동으로 생성된 생성시간 확인
        Assertions.assertThat(savedBoard.getCreatedAt()).isNotNull();

        System.out.println("저장 후 board : " + savedBoard);

        // 4. 원본 객체와 반환된 객체가 동일한 참조인지 확인
        // 영속성 컨텍스트는 같은 엔티티에 대해 같은 인스턴스를 보장
        Assertions.assertThat(board).isSameAs(savedBoard);
    }
}
