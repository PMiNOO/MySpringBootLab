package com.rookies4.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "books") // 테이블명을 'books'로 지정
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    // 정보 수정을 위한 Setter 메서드
    public void updateBook(String title, String author, Integer price, LocalDate publishDate) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.publishDate = publishDate;
    }
}