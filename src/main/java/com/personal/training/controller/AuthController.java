package com.personal.training.controller;

import com.personal.training.dto.Login.LoginRequestDTO;
import com.personal.training.dto.Login.LoginResponseDTO;
import com.personal.training.dto.Login.RefreshTokenDTO;
import com.personal.training.dto.Login.TrocaSenhaDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Operações relacionadas ao Login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Login - Autenticar Usuário",
            description = "Fazer login no sistema com credenciais de Usário"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Credenciais incorretas")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) throws RecursoNaoEncontradoException {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh Login - Autenticar Usuário",
            description = "Fazer refresh do login no sistema com Refresh Token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Refresh Token Incorreto")
    })
    public ResponseEntity<LoginResponseDTO> refresh(
            @RequestBody RefreshTokenDTO dto) throws RecursoNaoEncontradoException{

        return ResponseEntity.ok(authService.refreshTokenLogin(dto));
    }
}