package com.companyName.projectName.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        List<String> errorMessages = new ArrayList<>();
//        for (FieldError fieldError : fieldErrors) {
//            String errorMessage = String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
//            errorMessages.add(errorMessage);
//        }
//        return new ErrorResponse("Validation failed", errorMessages);
//    }
//
//    public static class ErrorResponse {
//        private String message;
//        private List<String> errors;
//
//        public ErrorResponse(String message, List<String> errors) {
//            this.message = message;
//            this.errors = errors;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public List<String> getErrors() {
//            return errors;
//        }
//    }
//}
