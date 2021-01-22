package com.test.exception;

import com.test.dto.DTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<DTO> handleAllExceptions(Exception ex, WebRequest request) {
    logger.error("Error in the API, ", ex);
    return new ResponseEntity<>(new DTO(1), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
