package com.tenco.blog.board;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

    // 의존성 주입: 스프링이 BoardNativeRepository 객체를 자동으로 주입
    // private final BoardNativeRepository boardNativeRepository;

    // V2에서는 PersistRepository 사용
    @Autowired // DI
    private BoardPersistRepository boardPersistRepository;


    @GetMapping("/board/save-form")
    public String saveForm() {
        // 게시글 작성 폼을 보여주는 뷰 반환
        // templates/board/save-form.html 파일을 렌더링
        return "board/save-form";
    }

    // 참고 사항
    // 1. DispatcherServlet이 요청 수신
    // 2. HandlerMapping이 적절한 Controller 메서드 찾기
    // 3. HandlerAdapter가 메서드 파라미터 분석
    // .....
    // 4. ArgumentResolver 동작

    // 게시글 저장: DTO 패턴과 영속성 컨텍스트 활용
    @PostMapping("/board/save")
    // Spring이 폼 데이터를 객체로 변환하는 과정 (Spring의 데이터 바인딩 메커니즘)
    // 폼 데이터 바인딩: Spring이 HTTP 요청 파라미터를 객체로 자동 변환
    // HandlerMethodArgumentResolver가 객체 생성 및 프로퍼티 설정 담당
    public String save(BoardRequest.SaveDTO reqDTO){
        // HTTP 요청: title=값&content=값&username=값 (application/x-www-form-urlencoded)
        // Spring 처리: new SaveDTO() 생성 후 setter 메서드들 자동 호출

        // 1. DTO에서 Entity로 변환
        //    - 계층 간 데이터 전송을 위한 DTO 사용
        //    - toEntity() 메서드로 명확한 변환 로직
        Board board = reqDTO.toEntity();

        // 2. 영속성 컨텍스트를 통한 엔티티 저장
        //    - V1: 직접 SQL 작성 및 실행
        //    - V2: JPA EntityManager의 persist() 사용
        Board savedBoard = boardPersistRepository.save(board);

        // 3. 저장된 엔티티는 영속 상태로 관리됨
        //    - 자동 생성된 ID와 생성시간 포함
        //    - 영속성 컨텍스트에서 변경 감지(Dirty Checking) 적용

        // 4. PRG 패턴으로 메인 페이지로 리다이렉트
        return "redirect:/";
    }



}

