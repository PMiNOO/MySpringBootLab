package com.rookies4.myspringbootlab.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class BookDTO {

  @Getter
  @Setter
  public static class BookCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Price is required")
    private Integer price;

    @NotNull(message = "Publish date is required")
    private LocalDate publishDate;
  }

  @Getter
  @Setter
  public static class BookUpdateRequest {
    private String title;
    private String author;
    private Integer price;
    private LocalDate publishDate;
  }

  @Getter
  public static class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private LocalDate publishDate;

    public BookResponse(com.rookies4.myspringbootlab.entity.Book book) {
      this.id = book.getId();
      this.title = book.getTitle();
      this.author = book.getAuthor();
      this.isbn = book.getIsbn();
      this.price = book.getPrice();
      this.publishDate = book.getPublishDate();
    }
  }
}