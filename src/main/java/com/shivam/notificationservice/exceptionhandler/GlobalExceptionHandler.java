package com.shivam.notificationservice.exceptionhandler;

import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.ResponseBody.ResponseError;
import com.shivam.notificationservice.exception.BadPageRequestException;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.exception.RepositoryException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null;
        String errorMessage = fieldError.getDefaultMessage();
        GenericResponse<Object,String,Object> errorResponse = new GenericResponse<>(null,errorMessage,null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = "Failed to read request. Please check the request format and try again.";
        GenericResponse<?,?,?> errorResponse = new GenericResponse<>(null,new ResponseError(errorMessage, ex.getMessage()),null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException badRequestException){
        GenericResponse<Object,ResponseError,Object> response = new GenericResponse<>(null,badRequestException.getResponseError(),null);
        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<Object> handleRepositoryException(RepositoryException repositoryException){
        GenericResponse<?,String,?> response = new GenericResponse<>(null, repositoryException.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadPageRequestException.class)
    public ResponseEntity<Object> handleBadPageRequestException(BadPageRequestException badPageRequestException){
        GenericResponse<?,String,?> response = new GenericResponse<>(null, badPageRequestException.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
