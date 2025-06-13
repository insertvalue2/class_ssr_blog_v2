package com.tenco.blog.repository;

import com.tenco.blog.model.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

// @Import: 테스트에서 사용할 커스텀 클래스를 추가로 로드
// BoardNativeRepository는 우리가 만든 클래스이므로 명시적으로 임포트 필요
// Spring Boot는 기본적으로 @Repository가 붙은 클래스만 자동 스캔
@Import(BoardNativeRepository.class)

// @DataJpaTest: JPA 관련 테스트만 로드하는 슬라이스 테스트
// 장점: 전체 애플리케이션 컨텍스트를 로드하지 않아 테스트 속도가 빠름
// 자동 설정: H2 인메모리 데이터베이스, EntityManager, TestEntityManager 등
// 트랜잭션: 각 테스트 메서드가 끝나면 자동으로 롤백(데이터 초기화)
@DataJpaTest
public class BoardNativeRepositoryTest {

    // @Autowired: 테스트에서 의존성 주입
    // Spring 테스트 컨텍스트에서 BoardNativeRepository 인스턴스를 자동 주입
    // 필드 주입 방식 (테스트에서는 생성자 주입보다 필드 주입이 간편)
    @Autowired
    private BoardNativeRepository boardNativeRepository;

    @Test
    public void findAll_test(){
        // given: 테스트를 위한 준비 단계
        // data.sql 파일에 의해 4개의 더미 데이터가 이미 삽입됨
        // @DataJpaTest가 자동으로 data.sql을 실행하여 테스트 데이터 준비

        // when: 실제 테스트할 행동
        // findAll() 메서드를 실행하여 모든 게시글을 조회
        List<Board> boardList = boardNativeRepository.findAll();

        // then: 결과 검증
        // 디버깅을 위한 출력 (실제 운영에서는 로그 사용 권장)
        System.out.println("findAll_test/size : "+boardList.size());
        System.out.println("findAll_test/username : "+boardList.get(2).getUsername());

        // AssertJ 라이브러리를 사용한 검증
        // 장점: 더 직관적이고 읽기 쉬운 테스트 코드 작성 가능
        // JUnit 기본 assertEquals보다 에러 메시지가 명확함
        Assertions.assertThat(boardList.size()).isEqualTo(4);
        // 실제 값 - ssar , 예상 값 cos
        Assertions.assertThat(boardList.get(2).getUsername()).isEqualTo("cos");

        // 다양한 AssertJ 검증 메서드
        // .isNotNull() - null이 아닌지 확인
        // .isGreaterThan(3) - 3보다 큰지 확인
        // .contains("제목1") - 특정 요소를 포함하는지 확인
    }

    // 추가 테스트 예시: 예외 상황 테스트
    @Test
    public void findAll_empty_test(){
        // given: 데이터를 모두 삭제하여 빈 상태 만들기
        // (실제로는 @Sql 어노테이션을 사용하여 특정 SQL 실행 가능)

        // when
        List<Board> boardList = boardNativeRepository.findAll();

        // then: 빈 리스트인지 확인
        Assertions.assertThat(boardList).isEmpty();
        // 또는 Assertions.assertThat(boardList.size()).isEqualTo(0);
    }

    // 단건 조회 테스트: 특정 ID로 게시글 조회 기능 검증
    @Test
    public void findById_test(){
        // given: 테스트할 게시글 ID 준비
        // data.sql에 의해 id=1인 게시글이 존재한다고 가정
        int id = 1;

        // when: findById 메서드 실행
        Board board = boardNativeRepository.findById(id);

        // 디버깅용 출력 (선택사항)
        // System.out.println("findById_test "+board);

        // then: 조회된 게시글의 내용이 예상과 일치하는지 검증
        // data.sql의 첫 번째 게시글 데이터와 비교
        Assertions.assertThat(board.getTitle()).isEqualTo("제목1");
        Assertions.assertThat(board.getContent()).isEqualTo("내용1");
        Assertions.assertThat(board.getUsername()).isEqualTo("ssar");

        // 추가 검증: 객체가 null이 아닌지 확인
        Assertions.assertThat(board).isNotNull();
        Assertions.assertThat(board.getId()).isEqualTo(1);
    }

    @Test
    public void deleteById_test() {
        // given
        int id = 1;

        // when - 게시글 pk 1을 삭제 요청 (샘플 데이터 4개에서 3개로)
        // 즉, deleteById() 메서드를 실행 시켜 봄 
        boardNativeRepository.deleteById(id);
        
        // then
        // deleteById() 메서드가 정상 동작해서 예상 결과값이 맞는가 확인
        List<Board> boardList = boardNativeRepository.findAll();
        Assertions.assertThat(boardList.size()).isEqualTo(3);
    }

    // 게시글 수정 기능 테스트
    @Test
    public void updateById_test(){
        // given: 수정할 데이터 준비
        // data.sql에 의해 id=1인 게시글이 존재한다고 가정
        int id = 1;
        String title = "제목수정1";      // 기존: "제목1" → 수정: "제목수정1"
        String content = "내용수정1";    // 기존: "내용1" → 수정: "내용수정1"
        String username = "bori";       // 기존: "ssar" → 수정: "bori"

        // when: (방금 만들었던) 실제 수정 실행
        boardNativeRepository.updateById(id, title, content, username);

        // then: 수정 결과 검증
        // 수정 후 다시 조회하여 변경사항이 올바르게 적용되었는지 확인
        Board board = boardNativeRepository.findById(id);

        // 디버깅용 출력: 수정된 게시글 전체 정보 확인
        System.out.println("updateById_test/board : " + board);

        // AssertJ를 사용한 검증: 각 필드가 예상값과 일치하는지 확인
        Assertions.assertThat(board.getTitle()).isEqualTo("제목수정1");
        Assertions.assertThat(board.getContent()).isEqualTo("내용수정1");
        Assertions.assertThat(board.getUsername()).isEqualTo("bori");

        // 추가 검증: 수정되지 않아야 할 필드들 확인
        Assertions.assertThat(board.getId()).isEqualTo(1);           // ID는 변경되지 않음
        Assertions.assertThat(board.getCreatedAt()).isNotNull();     // 생성시간은 유지됨
    }

}
