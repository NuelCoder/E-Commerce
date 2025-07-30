package com.example.pcuhub.pcuhub.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(BadInputException.class)
   public ResponseEntity<Map<String,Object>>handleBadInput(BadInputException ex){
      Map<String,Object>error = new HashMap<>();
      error.put("Error: ", ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(BadRequestException.class)
   public ResponseEntity<Map<String,Object>>handleBadRequest(BadRequestException ex){
      Map<String,Object> error = new HashMap<>();
      error.put("Error: ", ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(PendingUserException.class)
   public ResponseEntity<Map<String,Object>>handlePendingUserException(PendingUserException ex){
      Map<String,Object> error = new HashMap<>();
      error.put("Error: ", ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }
}
