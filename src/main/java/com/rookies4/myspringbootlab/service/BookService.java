package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Transactional
  public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
    Book book =
        Book.builder()
            .title(request.getTitle())
            .author(request.getAuthor())
            .isbn(request.getIsbn())
            .price(request.getPrice())
            .publishDate(request.getPublishDate())
            .build();
    Book savedBook = bookRepository.save(book);
    return new BookDTO.BookResponse(savedBook);
  }

  public List<BookDTO.BookResponse> getAllBooks() {
    return bookRepository.findAll().stream()
        .map(BookDTO.BookResponse::new)
        .collect(Collectors.toList());
  }

  public BookDTO.BookResponse getBookById(Long id) {
    Book book =
        bookRepository
            .findById(id)
            .orElseThrow(
                () -> new BusinessException("Book not found with ID: " + id, HttpStatus.NOT_FOUND));
    return new BookDTO.BookResponse(book);
  }

  public BookDTO.BookResponse getBookByIsbn(String isbn) {
    Book book =
        bookRepository
            .findByIsbn(isbn)
            .orElseThrow(
                () ->
                    new BusinessException(
                        "Book not found with ISBN: " + isbn, HttpStatus.NOT_FOUND));
    return new BookDTO.BookResponse(book);
  }

  @Transactional
  public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
    Book book =
        bookRepository
            .findById(id)
            .orElseThrow(
                () -> new BusinessException("Book not found with ID: " + id, HttpStatus.NOT_FOUND));
    book.updateBook(
        request.getTitle(), request.getAuthor(), request.getPrice(), request.getPublishDate());
    return new BookDTO.BookResponse(book);
  }

  @Transactional
  public void deleteBook(Long id) {
    Book book =
        bookRepository
            .findById(id)
            .orElseThrow(
                () -> new BusinessException("Book not found with ID: " + id, HttpStatus.NOT_FOUND));
    bookRepository.delete(book);
  }
}