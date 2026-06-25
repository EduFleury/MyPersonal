package com.personal.training.controller;

import com.personal.training.dto.Login.TrocaSenhaDTO;
import com.personal.training.dto.Usuario.UsuarioRequestDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.dto.Usuario.UsuarioResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.exception.SenhaIncorretaException;
import com.personal.training.service.AuthService;
import com.personal.training.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Operações relacionadas aos usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar usuário",
            description = "Realiza o cadastro de um novo usuário no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public UsuarioResponseDTO criar(
            @RequestBody @Valid UsuarioRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return usuarioService.criar(dto);
    }

    @GetMapping
    @Operation(
            summary = "Listar usuários",
            description = "Retorna todos os usuários cadastrados de forma paginada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public Page<UsuarioResponseDTO> listarTodos(@PageableDefault(size = 10, page = 0) Pageable pageable) {

        return usuarioService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public UsuarioResponseDTO buscarPorId(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/findByEmail")
    @Operation(
            summary = "Buscar usuário por e-mail",
            description = "Retorna um usuário através do e-mail informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public UsuarioResponseDTO buscarPorEmail(
            @RequestBody @Valid UsuarioRequestFindByEmailDTO dto
    ) throws RecursoNaoEncontradoException {

        return usuarioService.buscarPorEmail(dto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public UsuarioResponseDTO atualizar(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return usuarioService.atualizar(id, dto);
    }

    @PatchMapping("/{id}/senha")
    @Operation(
            summary = "Trocar senha do usuário",
            description = "Realiza a alteração da senha informando a senha atual e a nova senha"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Senha atual incorreta")
    })
    public ResponseEntity<Void> trocarSenha(@Parameter(description = "ID do usuário", example = "1")
                                            @PathVariable Long id,
                                            @RequestBody TrocaSenhaDTO dto) throws SenhaIncorretaException, RecursoNaoEncontradoException {
        authService.trocarSenha(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário do sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public void deletar(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long id
    ) throws RegraNegocioException {

        usuarioService.deletar(id);
    }
}