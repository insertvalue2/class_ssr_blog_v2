package com.tenco.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// @Controller: 스프링이 이 클래스를 웹 컨트롤러로 인식하도록 하는 어노테이션
// 클라이언트의 HTTP 요청을 받아서 처리하는 역할
@Controller
public class UserController {

    // @GetMapping: HTTP GET 요청을 처리하는 어노테이션
    // "/join-form" 경로로 GET 요청이 오면 이 메서드가 실행됨
    @GetMapping("/join-form")
    public String joinForm() {
        // 문자열을 반환하면 해당 이름의 뷰(HTML 파일)를 찾아서 응답
        // "user/join-form" → src/main/resources/templates/user/join-form.html
        return "user/join-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        // 반환값이 뷰 이름이 됨 (뷰 리졸버가 실제 파일 경로로 변환)
        return "user/login-form";
    }

    @GetMapping("/user/update-form")
    public String updateForm() {
        // URL 경로와 뷰 이름은 다를 수 있음
        // URL: /user/update-form, 뷰: templates/user/update-form.html
        return "user/update-form";
    }

    @GetMapping("/logout")
    public String logout() {
        // "redirect:" 접두사를 사용하면 다른 URL로 리다이렉트
        // 뷰를 렌더링하지 않고 브라우저가 "/" 경로로 새로운 요청을 보냄
        // 즉, 리다이렉트는 서버가 클라이언트(브라우저)에게
        // "지금 요청한 URL 대신 다른 URL로 다시 요청해줘"라고 지시하는 HTTP 응답 방식입니다.
        //
        return "redirect:/";
    }
}