package com.personal.training.controller;

import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoRequestDTO;
import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.ExecucaoTreinoService;
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
@RequestMapping("/execucoes-treino")
@RequiredArgsConstructor
@Tag(name = "Execução Treino", description = "Operações relacionadas à Execução dos Treinos")
public class ExecucaoTreinoController {

    private final ExecucaoTreinoService execucaoTreinoService;

    @PostMapping
    @Operation(
            summary = "Registrar execução de treino",
            description = "Cria um novo registro de execução de treino para um aluno"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Execução criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno ou treino não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public ExecucaoTreinoResponseDTO criar(
            @RequestBody @Valid ExecucaoTreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return execucaoTreinoService.criar(dto);
    }

    @GetMapping
    @Operation(
            summary = "Listar execuções",
            description = "Retorna todas as execuções de treino cadastradas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public List<ExecucaoTreinoResponseDTO> listar() {

        return execucaoTreinoService.listar();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar execução por ID",
            description = "Retorna os detalhes de uma execução de treino específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Execução encontrada"),
            @ApiResponse(responseCode = "404", description = "Execução não encontrada")
    })
    public ExecucaoTreinoResponseDTO buscarPorId(
            @Parameter(description = "ID da execução do treino", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return execucaoTreinoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar execução",
            description = "Atualiza os dados de uma execução de treino existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Execução atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Execução não encontrada"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public ExecucaoTreinoResponseDTO atualizar(
            @Parameter(description = "ID da execução do treino", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid ExecucaoTreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return execucaoTreinoService.atualizar(id, dto);
    }

    @PutMapping("/{id}/concluir")
    @Operation(
            summary = "Concluir treino",
            description = "Marca uma execução de treino como concluída"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treino concluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Execução não encontrada"),
            @ApiResponse(responseCode = "422", description = "Treino já concluído")
    })
    public ExecucaoTreinoResponseDTO concluir(
            @Parameter(description = "ID da execução do treino", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return execucaoTreinoService.concluir(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir execução",
            description = "Remove uma execução de treino do sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Execução removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Execução não encontrada")
    })
    public void excluir(
            @Parameter(description = "ID da execução do treino", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        execucaoTreinoService.excluir(id);
    }
}