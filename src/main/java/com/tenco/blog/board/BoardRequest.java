package com.tenco.blog.board;


import lombok.Data;

// 요청 데이터를 담는 DTO 클래스
// 컨트롤러와 비즈니스 로직 사이의 데이터 전송 객체
public class BoardRequest {

    // 정적 내부 클래스로 기능별 DTO 분리
    // SaveDTO: 게시글 저장 요청 데이터
    @Data
    public static class SaveDTO {
        private String title;
        private String content;
        private String username;

        // DTO에서 Entity로 변환하는 메서드
        // 계층 간 데이터 변환을 명확하게 분리
        // 비즈니스 로직(Entity 생성)을 DTO에서 캡슐화
        public Board toEntity(){
            return new Board(title, content, username);
        }
    }
}
