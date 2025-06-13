package com.tenco.blog.mustache;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * 주소 설계 : http://localhost:8080/test/home
     * @param model
     */
    @GetMapping("/test/home")
    public String home(Model model) {
        model.addAttribute("title", "스프링 부트 블로그");
        model.addAttribute("welcomeMessage", "환영합니다!");
        // 1. 코드 추가
        model.addAttribute("posts", new String[]{"첫 번째 글", "두 번째 글", "세 번째 글"});
        // 2. 코드 추가
        model.addAttribute("emptyPosts", new String[]{});

        return "test/home"; // templates/test/home.mustache로 매핑
    }

}
