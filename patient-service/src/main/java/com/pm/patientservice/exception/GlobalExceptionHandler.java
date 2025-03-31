package com.pm.patientservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String,String> map=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error)->{
            map.put(error.getField(), error.getDefaultMessage());
        });
        log.warn(
                "Validation error: {}",
                map
        );
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String,String>> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        Map<String,String> map=new HashMap<>();
        map.put("email", ex.getMessage());
        log.warn(
                "Email already exists: {}",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(map);
    }
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String,String>> handlePatientNotFoundException(PatientNotFoundException ex) {
        Map<String,String> map=new HashMap<>();
        map.put("patient", ex.getMessage());
        log.warn(
                "Patient not found: {}",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(map);
    }

}
