package com.personal.training.dto.Error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErroResponseDTO(LocalDateTime now, HttpStatus erro, String message) {
}
