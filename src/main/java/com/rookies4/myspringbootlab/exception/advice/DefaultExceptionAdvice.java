package com.rookies4.myspringbootlab.exception.advice;

import com.rookies4.myspringbootlab.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(BusinessException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(ex.getHttpStatus().value()); // e.g., 404
        errorObject.setMessage(ex.getMessage());

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(errorObject, HttpStatusCode.valueOf(ex.getHttpStatus().value()));
    }

    // 숫자 타입의 값에 문자열 타입의 값을 입력으로 받았을 때 발생하는 오류 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleException(HttpMessageNotReadableException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", e.getMessage());
        result.put("httpStatus", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e, WebRequest request) {
        log.error(e.getMessage(), e);

        // REST API 요청인지 확인 (Accept 헤더에 application/json이 포함되었거나 /api/로 시작하는 경로인 경우)
        if (isApiRequest(request)) {
            ErrorObject errorObject = new ErrorObject();
            errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorObject.setMessage(e.getMessage());
            return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // Thymeleaf 요청인 경우 ModelAndView로 500.html 반환
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error/500");
            modelAndView.addObject("error", e);
            return modelAndView;
        }
    }

    private boolean isApiRequest(WebRequest request) {
        // 요청 경로가 /api/로 시작하는지 확인
        String path = request.getDescription(false);
        if (path != null && path.startsWith("uri=/api/")) {
            return true;
        }
        // Accept 헤더 확인
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }

    // 입력 항목을 검증할 때 발생하는 오류를 처리하는 메서드 (BAD_REQUEST 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        ValidationErrorResponse response =
                new ValidationErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "입력항목 검증 오류",
                        LocalDateTime.now(),
                        errors
                );
        // badRequest()는 status code 400을 의미합니다.
        return ResponseEntity.badRequest().body(response);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ValidationErrorResponse {
        // 에러 코드
        private int status;
        // 에러 메시지
        private String message;
        // 에러 발생 시간
        private LocalDateTime timestamp;
        // 개별 항목의 에러 정보
        private Map<String, String> errors;
    }
}