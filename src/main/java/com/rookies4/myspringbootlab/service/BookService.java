package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import com.rookies4.myspringbootlab.exception.BusinessException;
// ErrorCode를 임포트합니다.
import com.rookies4.myspringbootlab.exception.ErrorCode;
import com.rookies4.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    private Book findBookById(Long id) {
        // 기존: new BusinessException("Book Not Found with id: " + id, HttpStatus.NOT_FOUND)
        // 변경: ErrorCode 사용
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            // 기존: new BusinessException("ISBN already exists: " + request.getIsbn(), HttpStatus.CONFLICT)
            // 변경: ErrorCode 사용
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        BookDetail bookDetail = BookDetail.builder()
                .description(request.getDetailRequest().getDescription())
                .language(request.getDetailRequest().getLanguage())
                .pageCount(request.getDetailRequest().getPageCount())
                .publisher(request.getDetailRequest().getPublisher())
                .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                .edition(request.getDetailRequest().getEdition())
                .build();

        book.setBookDetail(bookDetail);
        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    // ... (getAllBooks 메서드는 변경 없음) ...
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }


    public BookDTO.Response getBookById(Long id) {
        // 기존: new BusinessException("Book Not Found with id: " + id, HttpStatus.NOT_FOUND)
        // 변경: ErrorCode 사용
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        // 기존: new BusinessException("Book Not Found with isbn: " + isbn, HttpStatus.NOT_FOUND)
        // 변경: ErrorCode 사용
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "isbn", isbn));
        return BookDTO.Response.fromEntity(book);
    }

    // ... (searchBooksByAuthor, searchBooksByTitle 메서드는 변경 없음) ...
    public List<BookDTO.Response> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookDTO.Response> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = findBookById(id);

        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            // 기존: new BusinessException("ISBN already exists: " + request.getIsbn(), HttpStatus.CONFLICT)
            // 변경: ErrorCode 사용
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        BookDetail bookDetail = book.getBookDetail();
        bookDetail.setDescription(request.getDetailRequest().getDescription());
        bookDetail.setLanguage(request.getDetailRequest().getLanguage());
        bookDetail.setPageCount(request.getDetailRequest().getPageCount());
        bookDetail.setPublisher(request.getDetailRequest().getPublisher());
        bookDetail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
        bookDetail.setEdition(request.getDetailRequest().getEdition());

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    // ... (patchBook, patchBookDetail 메서드 중 ISBN 중복 체크 부분 수정) ...
    @Transactional
    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest request) {
        Book book = findBookById(id);

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getPublishDate() != null) book.setPublishDate(request.getPublishDate());

        if (request.getIsbn() != null && !book.getIsbn().equals(request.getIsbn())) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                // 기존: new BusinessException("ISBN already exists: " + request.getIsbn(), HttpStatus.CONFLICT)
                // 변경: ErrorCode 사용
                throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
            }
            book.setIsbn(request.getIsbn());
        }

        if (request.getDetailRequest() != null) {
            BookDetail bookDetail = book.getBookDetail();
            BookDTO.BookDetailPatchRequest detailRequest = request.getDetailRequest();
            if (detailRequest.getDescription() != null) bookDetail.setDescription(detailRequest.getDescription());
            if (detailRequest.getLanguage() != null) bookDetail.setLanguage(detailRequest.getLanguage());
            if (detailRequest.getPageCount() != null) bookDetail.setPageCount(detailRequest.getPageCount());
            if (detailRequest.getPublisher() != null) bookDetail.setPublisher(detailRequest.getPublisher());
            if (detailRequest.getCoverImageUrl() != null) bookDetail.setCoverImageUrl(detailRequest.getCoverImageUrl());
            if (detailRequest.getEdition() != null) bookDetail.setEdition(detailRequest.getEdition());
        }

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    @Transactional
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        Book book = findBookById(id);
        BookDetail bookDetail = book.getBookDetail();

        if (request.getDescription() != null) bookDetail.setDescription(request.getDescription());
        if (request.getLanguage() != null) bookDetail.setLanguage(request.getLanguage());
        if (request.getPageCount() != null) bookDetail.setPageCount(request.getPageCount());
        if (request.getPublisher() != null) bookDetail.setPublisher(request.getPublisher());
        if (request.getCoverImageUrl() != null) bookDetail.setCoverImageUrl(request.getCoverImageUrl());
        if (request.getEdition() != null) bookDetail.setEdition(request.getEdition());

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }


    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            // 기존: new BusinessException("Book Not Found with id: " + id, HttpStatus.NOT_FOUND)
            // 변경: ErrorCode 사용
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id);
        }
        bookRepository.deleteById(id);
    }
}