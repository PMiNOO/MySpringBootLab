package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookRepository bookRepository;

    /**
     * POST /api/books : 새 도서 등록
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    /**
     * GET /api/books : 모든 도서 조회
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * GET /api/books/{id} : ID로 특정 도서 조회
     * Optional 클래스의 map() / orElse() 사용
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/books/isbn/{isbn}/ : ISBN으로 도서 조회
     * BusinessException과 ErrorObject / DefaultExceptionAdvice 사용
     */
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }

    /**
     * PUT /api/books/{id}: 도서 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        existBook.setTitle(bookDetail.getTitle());
        existBook.setAuthor(bookDetail.getAuthor());
        existBook.setIsbn(bookDetail.getIsbn());
        existBook.setPrice(bookDetail.getPrice());
        existBook.setPublishDate(bookDetail.getPublishDate());

        Book updatedBook = bookRepository.save(existBook);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * DELETE /api/books/{id} : 도서 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("Book Not Found", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}