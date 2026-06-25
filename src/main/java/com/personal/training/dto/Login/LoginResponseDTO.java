package com.personal.training.dto.Login;

public record LoginResponseDTO(String accessToken, String refreshToken, String tipo, Long id) {}