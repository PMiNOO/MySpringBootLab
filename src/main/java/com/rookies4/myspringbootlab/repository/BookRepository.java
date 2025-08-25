package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    // 저자 이름으로 책 검색 (대소문자 무시)
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // 제목으로 책 검색 (대소문자 무시)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // ISBN 중복 확인
    boolean existsByIsbn(String isbn);

    // ID로 책 조회 시 BookDetail을 함께 Fetch Join
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    // ISBN으로 책 조회 시 BookDetail을 함께 Fetch Join
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);
}