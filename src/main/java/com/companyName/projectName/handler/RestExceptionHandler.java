package com.companyName.projectName.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
//public class RestExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(value = { ValidationErrorException.class })
//    protected ResponseEntity<Object> handleValidationError(
//            ValidationErrorException ex, WebRequest request) {
//        List<ErrorDetail> errorDetails = ex.getErrorDetails();
//        ErrorResponse errorResponse = new ErrorResponse(
//                new Date(), HttpStatus.BAD_REQUEST.value(),
//                HttpStatus.BAD_REQUEST.getReasonPhrase(), errorDetails,
//                request.getDescription(false));
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//}