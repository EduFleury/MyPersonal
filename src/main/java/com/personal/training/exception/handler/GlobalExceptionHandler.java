package com.personal.training.exception.handler;

import com.personal.training.dto.Error.ErroResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.exception.SenhaIncorretaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponseDTO> handleRegraNegocio(
            RegraNegocioException ex) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        ex.getMessage()));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleNotFound(
            RecursoNaoEncontradoException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND,
                        ex.getMessage()));
    }

    @ExceptionHandler(SenhaIncorretaException.class)
    public ResponseEntity<ErroResponseDTO> handleRegraNegocio(
            SenhaIncorretaException ex) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        ex.getMessage()));
    }
}