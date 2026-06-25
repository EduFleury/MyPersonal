package com.personal.training.controller;

import com.personal.training.dto.ItemTreino.ItemTreinoRequestDTO;
import com.personal.training.dto.ItemTreino.ItemTreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.service.ItemTreinoService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-treino")
@RequiredArgsConstructor
@Tag(
        name = "Item Treino",
        description = "Operações relacionadas aos itens de treino, responsáveis por vincular exercícios a um treino."
)
public class ItemTreinoController {

    private final ItemTreinoService itemTreinoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar item de treino",
            description = "Adiciona um exercício a um treino, criando um novo item de treino."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item de treino cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Treino ou exercício não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ItemTreinoResponseDTO criar(
            @RequestBody @Valid ItemTreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return itemTreinoService.criar(dto);
    }

    @GetMapping
    @Operation(
            summary = "Listar itens de treino",
            description = "Retorna todos os itens de treino cadastrados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public Page<ItemTreinoResponseDTO> listar(@PageableDefault(size = 10, page = 0) Pageable pageable) {

        return itemTreinoService.listar(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar item de treino por ID",
            description = "Retorna os dados de um item de treino específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item de treino encontrado"),
            @ApiResponse(responseCode = "404", description = "Item de treino não encontrado")
    })
    public ItemTreinoResponseDTO buscarPorId(
            @Parameter(description = "ID do item de treino", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return itemTreinoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar item de treino",
            description = "Atualiza os dados de um item de treino existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item de treino atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item de treino não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ItemTreinoResponseDTO atualizar(
            @Parameter(description = "ID do item de treino", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid ItemTreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return itemTreinoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir item de treino",
            description = "Remove um item de treino do sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item de treino removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item de treino não encontrado")
    })
    public void excluir(
            @Parameter(description = "ID do item de treino", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        itemTreinoService.excluir(id);
    }
}