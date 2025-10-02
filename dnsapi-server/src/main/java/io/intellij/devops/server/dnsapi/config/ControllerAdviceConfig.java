package io.intellij.devops.server.dnsapi.config;

import io.intellij.devops.server.dnsapi.entities.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * ControllerAdviceConfig
 *
 * @author tech@intellij.io
 */
@ControllerAdvice
@Slf4j
public class ControllerAdviceConfig {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<Object>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("catch NoHandlerFoundException|{}", e.getMessage());
        return ResponseEntity.ok(Result.fail("NOT FOUND"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Object>> handleException(Exception e) {
        log.error("catch Exception|{}", e.getMessage());
        return ResponseEntity.ok(Result.fail(e.getMessage()));
    }

}
