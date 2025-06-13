package com.tenco.blog.model;

import com.tenco.blog.utils.MyDateUtil;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

// @Data: Lombok이 getter, setter, toString 등을 자동 생성
// 개발 생산성을 높이는 어노테이션
@Data
// @Table: 실제 데이터베이스 테이블명을 지정
// 클래스명과 테이블명이 다를 때 사용
@Table(name = "board_tb")
// @Entity: JPA가 이 클래스를 데이터베이스 테이블과 매핑하는 엔티티로 인식
// 이 어노테이션이 있어야 JPA가 관리함
@Entity
public class Board {

    // @Id: 이 필드가 기본키(Primary Key)임을 나타냄
    @Id
    // @GeneratedValue: 기본키 값을 자동으로 생성
    // IDENTITY 전략: 데이터베이스의 AUTO_INCREMENT 기능 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 별도 어노테이션이 없으면 필드명이 컬럼명이 됨
    private String title;       // title 컬럼
    private String content;     // content 컬럼
    private String username;    // username 컬럼
    private Timestamp createdAt; // created_at 컬럼 (스네이크 케이스로 자동 변환)


    // Mustache 템플릿에서 {{time}}으로 사용하기 위한 getter 메서드
    // 메서드명이 getTime()이므로 템플릿에서는 {{time}}으로 접근
    // 비즈니스 로직을 엔티티에 포함시키는 Domain Model 패턴
    public String getTime(){
        return MyDateUtil.timestampFormat(createdAt);
    }
}