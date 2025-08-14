package com.rookies4.myspringbootlab;

import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("도서 등록 테스트")
    void testCreateBook() {
        // given
        Book newBook = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();

        // when
        Book savedBook = bookRepository.save(newBook);

        // then
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("스프링 부트 입문");
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트")
    void testFindByIsbn() {
        // given
        String isbn = "9788956746425";
        Book book = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn(isbn)
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();
        bookRepository.save(book);

        // when
        Optional<Book> foundBook = bookRepository.findByIsbn(isbn);

        // then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getIsbn()).isEqualTo(isbn);
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트")
    void testFindByAuthor() {
        // given
        String author = "박둘리";
        bookRepository.save(Book.builder()
                .title("JPA 프로그래밍")
                .author(author)
                .isbn("9788956746432")
                .price(35000)
                .publishDate(LocalDate.of(2025, 4, 30))
                .build());
        bookRepository.save(Book.builder()
                .title("또 다른 책")
                .author(author)
                .isbn("9788956746449")
                .price(25000)
                .publishDate(LocalDate.of(2024, 1, 1))
                .build());

        // when
        List<Book> books = bookRepository.findByAuthor(author);

        // then
        assertThat(books).hasSize(2);
        assertThat(books.get(0).getAuthor()).isEqualTo(author);
    }

    @Test
    @DisplayName("도서 정보 수정 테스트")
    void testUpdateBook() {
        // given
        Book originalBook = bookRepository.save(Book.builder()
                .title("JPA 프로그래밍")
                .author("박둘리")
                .isbn("9788956746432")
                .price(35000)
                .publishDate(LocalDate.of(2025, 4, 30))
                .build());

        // when
        Book foundBook = bookRepository.findById(originalBook.getId()).get();
        foundBook.updateBook("수정된 JPA 프로그래밍", "김둘리", 40000, LocalDate.of(2025, 5, 1));
        Book updatedBook = bookRepository.save(foundBook);

        // then
        assertThat(updatedBook.getTitle()).isEqualTo("수정된 JPA 프로그래밍");
        assertThat(updatedBook.getAuthor()).isEqualTo("김둘리");
        assertThat(updatedBook.getPrice()).isEqualTo(40000);
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    void testDeleteBook() {
        // given
        Book bookToDelete = bookRepository.save(Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build());
        Long id = bookToDelete.getId();

        // when
        bookRepository.deleteById(id);

        // then
        Optional<Book> deletedBook = bookRepository.findById(id);
        assertThat(deletedBook).isNotPresent();
    }
}