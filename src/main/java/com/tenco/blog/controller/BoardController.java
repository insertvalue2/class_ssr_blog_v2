package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    // 의존성 주입: 스프링이 BoardNativeRepository 객체를 자동으로 주입
    private final BoardNativeRepository boardNativeRepository;

    // 게시글 수정 처리 (실제 수정 액션)
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable(name = "id") Integer id, String title, String content, String username){
        // @PathVariable Integer id: URL 경로의 {id} 값 (수정할 게시글 ID)
        // String title, content, username: 폼에서 전송된 수정 데이터
        // 매개변수명이 폼의 name 속성과 일치하면 자동으로 바인딩됨

        // 디버깅용 출력: 전송받은 데이터 확인
        System.out.println("수정할 게시글 ID : " + id);
        System.out.println("수정된 제목 : " + title);
        System.out.println("수정된 내용 : " + content);
        System.out.println("수정된 작성자 : " + username);

        // Repository를 통해 실제 데이터베이스 수정 실행
        boardNativeRepository.updateById(id, title, content, username);

        // PRG 패턴 (Post-Redirect-Get) 적용
        // 수정 완료 후 해당 게시글 상세보기 페이지로 리다이렉트
        // "redirect:/board/" + id: 동적으로 리다이렉트 URL 생성
        return "redirect:/board/" + id;
    }


    // 게시글 수정 폼 페이지 요청 처리
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Integer id, HttpServletRequest request){
        // @PathVariable: URL 경로의 {id} 값을 메서드 매개변수로 받음
        // 수정할 게시글의 ID를 통해 기존 데이터를 조회

        // 기존 게시글 데이터를 조회하여 수정 폼에 미리 채워넣기 위함
        // 사용자가 현재 값을 확인하고 필요한 부분만 수정할 수 있음
        Board board = boardNativeRepository.findById(id);

        // 조회된 게시글 데이터를 뷰에 전달
        // 템플릿에서 {{board.title}}, {{board.content}} 등으로 사용 가능
        request.setAttribute("board", board);

        // board 폴더의 update-form.html 템플릿 렌더링
        return "board/update-form";
    }

    // @PostMapping: HTTP POST 요청을 처리
    // 폼에서 제출된 데이터를 받아서 처리
    @PostMapping("/board/save")
    public String save(String title, String content, String username){
        // 폼의 name 속성과 매개변수명이 일치하면 자동으로 값이 바인딩됨
        // name="title" → String title로 자동 매핑

        // Repository를 통해 데이터베이스에 저장
        boardNativeRepository.save(title, content, username);

        // redirect: 저장 후 메인 페이지로 이동
        // POST 요청 후 redirect로 PRG(Post-Redirect-Get) 패턴 구현
        return "redirect:/";
    }


    // 메인 페이지: 게시글 목록 보기
    // Model 클래스 대신 HttpServletRequest 사용해 보기
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        // Repository에서 모든 게시글 조회
        List<Board> boardList = boardNativeRepository.findAll();

        // HttpServletRequest를 사용해서 뷰에 데이터 전달
        // "boardList"라는 이름으로 템플릿에서 사용 가능
        // Model 객체를 사용하는 방법도 있음: Model model → model.addAttribute()
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        // 게시글 작성 폼을 보여주는 뷰 반환
        // templates/board/save-form.html 파일을 렌더링
        return "board/save-form";
    }

    // 프로젝트 내에서 동일한 URL 매핑을 설정하면 오류 발생 (동일한 주소 설계)
    // 게시글 상세보기: PathVariable을 사용한 동적 URL 처리
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Integer id, HttpServletRequest request) {

        // Integer 타입으로 자동 변환됨 (Spring의 타입 컨버전)
        // 숫자가 아닌 값이 들어오면 400 Bad Request 에러 발생

        // Repository에서 해당 ID의 게시글 조회
        Board board = boardNativeRepository.findById(id);

        // 조회된 게시글을 뷰에 전달
        // "board"라는 이름으로 템플릿에서 사용 가능
        request.setAttribute("board", board);

        // board 폴더의 detail.html 템플릿 렌더링
        return "board/detail";
    }

    // 게시글 삭제 처리
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id){
        // @PostMapping: HTTP POST 방식으로만 삭제 요청 처리
        // GET 방식 삭제는 보안상 위험 (URL 클릭만으로 삭제 가능)
        // POST 방식은 의도적인 form 제출을 통해서만 실행됨

        // @PathVariable Integer id: URL 경로의 {id} 값을 매개변수로 받음
        // 예: POST /board/1/delete → id = 1
        // 자동 타입 변환: String → Integer

        // Repository를 통해 실제 삭제 실행
        boardNativeRepository.deleteById(id);

        // PRG 패턴 (Post-Redirect-Get) 적용
        // 삭제 후 메인 페이지로 리다이렉트하여 중복 삭제 방지
        // 새로고침을 해도 삭제가 다시 실행되지 않음
        return "redirect:/";
    }



}

