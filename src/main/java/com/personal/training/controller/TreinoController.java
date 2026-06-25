package com.personal.training.controller;

import com.personal.training.dto.Treino.TreinoRequestDTO;
import com.personal.training.dto.Treino.TreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.TreinoService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treinos")
@RequiredArgsConstructor
@Tag(name = "Treino", description = "Operações relacionadas aos treinos")
public class TreinoController {

    private final TreinoService treinoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar treino",
            description = "Realiza o cadastro de um novo treino para um aluno"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Treino cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno ou personal trainer não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public TreinoResponseDTO criar(
            @RequestBody @Valid TreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {
        return treinoService.criar(dto);
    }

    @GetMapping
    @Operation(
            summary = "Listar treinos",
            description = "Retorna todos os treinos cadastrados de forma Paginada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public Page<TreinoResponseDTO> listar(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return treinoService.listar(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar treino por ID",
            description = "Retorna os dados de um treino específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treino encontrado"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado")
    })
    public TreinoResponseDTO buscarPorId(
            @Parameter(description = "ID do treino", example = "1")
            @PathVariable Long id
    ) throws RegraNegocioException {
        return treinoService.buscarPorId(id);
    }

    @GetMapping("/aluno/{alunoId}")
    @Operation(
            summary = "Listar treinos por aluno",
            description = "Retorna todos os treinos vinculados a um aluno específico de forma Paginada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public Page<TreinoResponseDTO> listarPorAluno(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable Long alunoId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return treinoService.listarPorAluno(alunoId, pageable);
    }

    @GetMapping("/personal/{personalId}")
    @Operation(
            summary = "Listar treinos por personal trainer",
            description = "Retorna todos os treinos cadastrados por um personal trainer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personal trainer não encontrado")
    })
    public Page<TreinoResponseDTO> listarPorPersonal(
            @Parameter(description = "ID do personal trainer", example = "1")
            @PathVariable Long personalId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return treinoService.listarPorPersonal(personalId, pageable);
    }

    @GetMapping("/meus-treinos/personal")
    @Operation(
            summary = "Listar meus treinos como personal",
            description = "Retorna todos os treinos cadastrados pelo personal trainer autenticado de forma Paginada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão")
    })
    public Page<TreinoResponseDTO> listarPorPersonal(
            Authentication authentication,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return treinoService.listarPorPersonalLogado(authentication.getName(), pageable);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar treino",
            description = "Atualiza os dados de um treino existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treino atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public TreinoResponseDTO atualizar(
            @Parameter(description = "ID do treino", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid TreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {
        return treinoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir treino",
            description = "Remove um treino do sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Treino removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado")
    })
    public void excluir(
            @Parameter(description = "ID do treino", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException{
        treinoService.excluir(id);
    }

    @GetMapping("/meus-treinos")
    @Operation(
            summary = "Listar meus treinos",
            description = "Retorna todos os treinos do aluno autenticado de forma Paginada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public Page<TreinoResponseDTO> meusTreinos(
            Authentication authentication,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) throws RecursoNaoEncontradoException{
        return treinoService.listarMeusTreinos(authentication.getName(), pageable);
    }
}