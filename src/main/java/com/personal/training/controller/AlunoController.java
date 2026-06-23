package com.personal.training.controller;

import com.personal.training.dto.Aluno.AlunoRequestDTO;
import com.personal.training.dto.Aluno.AlunoResponseDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
@Tag(name = "Aluno", description = "Endpoints responsáveis pelo gerenciamento de alunos")
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar aluno",
            description = "Realiza o cadastro de um novo aluno vinculado a um usuário existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public AlunoResponseDTO criar(
            @RequestBody @Valid AlunoRequestDTO dto
    ) throws RegraNegocioException, RecursoNaoEncontradoException {

        return alunoService.criar(dto);
    }

    @GetMapping
    @Operation(
            summary = "Buscar todos os alunos",
            description = "Retorna os dados de todos os alunos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno encontrado")
    })
    public List<AlunoResponseDTO> listarTodos() {

        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar aluno por ID",
            description = "Retorna os dados de um aluno específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public AlunoResponseDTO buscarPorId(
            @Parameter(description = "ID do Aluno", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return alunoService.buscarPorId(id);
    }

    @GetMapping("/usuario/{id}")
    @Operation(
            summary = "Buscar aluno por ID Usuário",
            description = "Retorna os dados de um aluno específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public AlunoResponseDTO buscarPorIdUsuario(
            @Parameter(description = "ID do Usuário", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return alunoService.buscarPorIdUsuario(id);
    }

    @GetMapping("/email")
    @Operation(
            summary = "Buscar aluno por e-mail",
            description = "Retorna um aluno através do e-mail do usuário"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public AlunoResponseDTO buscarPorEmailUsuario(
            @RequestBody @Valid UsuarioRequestFindByEmailDTO dto
    ) throws RecursoNaoEncontradoException {

        return alunoService.buscarPorEmailUsuario(dto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar aluno",
            description = "Atualiza os dados de um aluno existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public AlunoResponseDTO atualizar(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid AlunoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return alunoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Remover aluno",
            description = "Remove um aluno do sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aluno removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public void deletar(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        alunoService.deletar(id);
    }

    @GetMapping("/meus-alunos")
    @Operation(
            summary = "Listar meus alunos",
            description = "Retorna todos os alunos vinculados ao personal autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão")
    })
    public List<AlunoResponseDTO> meusAlunos(
            Authentication authentication
    ) {

        return alunoService.listarMeusAlunos(
                authentication.getName()
        );
    }
}