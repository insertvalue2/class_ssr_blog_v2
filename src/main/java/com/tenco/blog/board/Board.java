package com.tenco.blog.board;

import com.tenco.blog.utils.MyDateUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

// @NoArgsConstructor: JPA에서 엔티티는 기본 생성자가 필요
// JPA가 리플렉션을 통해 객체를 생성할 때 사용
@NoArgsConstructor
@Data
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String username;

    // @CreationTimestamp: Hibernate가 제공하는 어노테이션
    // 엔티티가 처음 저장될 때 현재 시간을 자동으로 설정
    // V1에서는 SQL에서 now()를 직접 사용했지만, V2에서는 JPA가 자동 처리
    // pc -> db (날짜주입)
    @CreationTimestamp
    private Timestamp createdAt;

    // 비즈니스 로직을 위한 생성자
    // id와 createdAt은 JPA가 자동으로 설정하므로 매개변수에서 제외
    public Board(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
        // id와 createdAt은 JPA/Hibernate가 자동으로 설정
    }

    public String getTime(){
        return MyDateUtil.timestampFormat(createdAt);
    }
}