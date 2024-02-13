package ru.skypro.homework.exceptions;

import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Класс, обрабатывающий глобальные исключения и возвращающий соответствующие HTTP-ответы.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Обработчик исключения `UsernameNotFoundException`.
     *
     * @param ex Исключение `UsernameNotFoundException`, которое необходимо обработать.
     * @return HTTP-ответ со статусом FORBIDDEN.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Void> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Обработчик внутреннего исключения `ExecutionControl.InternalException`.
     *
     * @param ex Внутреннее исключение `ExecutionControl.InternalException`, которое необходимо обработать.
     * @return HTTP-ответ со статусом INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(ExecutionControl.InternalException.class)
    public ResponseEntity<Void> handleInternalException(ExecutionControl.InternalException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Обработчик исключения `IllegalArgumentException`.
     *
     * @param ex Исключение `IllegalArgumentException`, которое необходимо обработать.
     * @return HTTP-ответ со статусом BAD_REQUEST.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
