package com.personal.training.controller;

import com.personal.training.dto.Login.TrocaSenhaDTO;
import com.personal.training.dto.Usuario.UsuarioRequestDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.dto.Usuario.UsuarioResponseDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AuthService;
import com.personal.training.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO criar(
            @RequestBody @Valid UsuarioRequestDTO dto
    ) throws RegraNegocioException {

        return usuarioService.criar(dto);
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {

        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarPorId(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/findByEmail")
    public UsuarioResponseDTO buscarPorEmail(
            @RequestBody @Valid UsuarioRequestFindByEmailDTO dto
    ) throws RegraNegocioException {

        return usuarioService.buscarPorEmail(dto);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO dto
    ) throws RegraNegocioException {

        return usuarioService.atualizar(id, dto);
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> trocarSenha(@PathVariable Long id,
                                            @RequestBody TrocaSenhaDTO dto) throws RegraNegocioException {
        authService.trocarSenha(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long id
    ) throws RegraNegocioException {

        usuarioService.deletar(id);
    }
}