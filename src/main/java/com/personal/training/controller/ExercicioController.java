package com.personal.training.controller;

import com.personal.training.dto.Exercicio.ExercicioRequestDTO;
import com.personal.training.dto.Exercicio.ExercicioResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.ExercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercicios")
@RequiredArgsConstructor
@Tag(name = "Exercício", description = "Operações relacionadas aos Exercícios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar exercício",
            description = "Realiza o cadastro de um novo exercício"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Exercício cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ExercicioResponseDTO criar(
            @RequestBody @Valid ExercicioRequestDTO dto
    ) {

        return exercicioService.criar(dto);
    }

    @GetMapping
    @Operation(
            summary = "Listar exercícios",
            description = "Retorna todos os exercícios cadastrados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public List<ExercicioResponseDTO> listar() {

        return exercicioService.listar();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar exercício por ID",
            description = "Retorna os dados de um exercício específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercício encontrado"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado")
    })
    public ExercicioResponseDTO buscarPorId(
            @Parameter(description = "ID do exercício", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return exercicioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar exercício",
            description = "Atualiza os dados de um exercício existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ExercicioResponseDTO atualizar(
            @Parameter(description = "ID do exercício", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid ExercicioRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return exercicioService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir exercício",
            description = "Remove um exercício do sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exercício removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado")
    })
    public void excluir(
            @Parameter(description = "ID do exercício", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        exercicioService.excluir(id);
    }
}