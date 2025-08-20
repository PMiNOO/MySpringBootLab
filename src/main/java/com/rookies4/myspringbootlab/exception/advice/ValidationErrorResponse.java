package com.rookies4.myspringbootlab.exception.advice;

import lombok.Data;
import java.util.Map;

@Data
public class ValidationErrorResponse {
    private int status;
    private String message;
    private String timestamp;
    private Map<String, String> errors;
}