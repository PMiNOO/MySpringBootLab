package com.rookies4.myspringbootlab.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 에러
    RESOURCE_NOT_FOUND("%s not found with %s: %s", HttpStatus.NOT_FOUND),

    // 책 관련 에러
    ISBN_DUPLICATE("Book already exists with ISBN: %s", HttpStatus.CONFLICT),

    // --- [과제] Publisher 관련 에러 코드 추가 ---
    PUBLISHER_NAME_DUPLICATE("Publisher already exists with name: %s", HttpStatus.CONFLICT),
    PUBLISHER_HAS_BOOKS("Cannot delete publisher with id: %s. It has %s books", HttpStatus.CONFLICT);
    // ----------------------------------------

    private final String messageTemplate;
    private final HttpStatus httpStatus;

    public String formatMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}