package com.shock.herogrind.shared.exception;

import com.shock.herogrind.hero.api.exception.HeroNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HeroNotFoundException.class)
    ResponseEntity<ApiError> handle(HeroNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(exception.getMessage()));
    }
}
