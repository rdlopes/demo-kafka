package io.github.rdlopes.kafka.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ProblemDetail handleRuntimeException(RuntimeException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setType(URI.create("https://api.demo-kafka.com/errors/internal-server-error"));
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGeneralException(Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    problemDetail.setTitle("Error");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }
}
