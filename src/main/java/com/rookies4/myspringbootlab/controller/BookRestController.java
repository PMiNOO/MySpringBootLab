package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response response = bookService.createBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {
        List<BookDTO.Response> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        BookDTO.Response book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        BookDTO.Response book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> searchBooksByAuthor(@RequestParam String author) {
        List<BookDTO.Response> books = bookService.searchBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> searchBooksByTitle(@RequestParam String title) {
        List<BookDTO.Response> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.Response> patchBook(@PathVariable Long id, @Valid @RequestBody BookDTO.PatchRequest request) {
        BookDTO.Response updatedBook = bookService.patchBook(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.Response> patchBookDetail(@PathVariable Long id, @Valid @RequestBody BookDTO.BookDetailPatchRequest request) {
        BookDTO.Response updatedBook = bookService.patchBookDetail(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}