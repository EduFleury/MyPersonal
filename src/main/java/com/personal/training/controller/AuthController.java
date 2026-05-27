package com.personal.training.controller;

import com.personal.training.dto.Login.LoginRequestDTO;
import com.personal.training.dto.Login.LoginResponseDTO;
import com.personal.training.dto.Login.TrocaSenhaDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) throws RegraNegocioException {
        return ResponseEntity.ok(authService.login(dto));
    }
}