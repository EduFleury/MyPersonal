package com.personal.training.controller;

import com.personal.training.dto.Aluno.AlunoResponseDTO;
import com.personal.training.dto.Personal.PersonalTrainerRequestDTO;
import com.personal.training.dto.Personal.PersonalTrainerResponseDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AlunoService;
import com.personal.training.service.PersonalTrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personais")
@RequiredArgsConstructor
@Tag(name = "Personal", description = "Operações relacionadas aos personais trainings")
public class PersonalTrainerController {

    private final PersonalTrainerService personalTrainerService;
    private final AlunoService alunoService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar personal trainer",
            description = "Realiza o cadastro de um novo personal trainer vinculado a um usuário existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Personal trainer cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public PersonalTrainerResponseDTO criar(
            @RequestBody @Valid PersonalTrainerRequestDTO dto
    ) throws RegraNegocioException, RecursoNaoEncontradoException {

        return personalTrainerService.criar(dto);
    }

    @GetMapping
    @Operation(
            summary = "Listar personal trainers",
            description = "Retorna todos os personal trainers cadastrados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public List<PersonalTrainerResponseDTO> listarTodos() {

        return personalTrainerService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar personal trainer por ID",
            description = "Retorna os dados de um personal trainer específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personal trainer encontrado"),
            @ApiResponse(responseCode = "404", description = "Personal trainer não encontrado")
    })
    public PersonalTrainerResponseDTO buscarPorId(
            @Parameter(description = "ID do personal trainer", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return personalTrainerService.buscarPorId(id);
    }

    @GetMapping("/usuario/{id}")
    @Operation(
            summary = "Buscar personal trainer por ID do usuário",
            description = "Retorna o personal trainer associado ao usuário informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personal trainer encontrado"),
            @ApiResponse(responseCode = "404", description = "Personal trainer não encontrado")
    })
    public PersonalTrainerResponseDTO buscarPorIdUsuario(
            @Parameter(description = "ID do Usuário", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        return personalTrainerService.buscarPorIdUsuario(id);
    }

    @GetMapping("/email")
    @Operation(
            summary = "Buscar personal trainer por e-mail",
            description = "Retorna um personal trainer através do e-mail do usuário"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personal trainer encontrado"),
            @ApiResponse(responseCode = "404", description = "Personal trainer não encontrado")
    })
    public PersonalTrainerResponseDTO buscarPorEmail(
            @RequestBody @Valid UsuarioRequestFindByEmailDTO dto
    ) throws RecursoNaoEncontradoException {

        return personalTrainerService.buscarPorEmail(dto);
    }

    @GetMapping("/alunos/{personalId}")
    @Operation(
            summary = "Listar alunos de um personal trainer",
            description = "Retorna todos os alunos vinculados ao personal trainer informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personal trainer não encontrado")
    })
    public ResponseEntity<List<AlunoResponseDTO>> buscarAlunosPorPersonalId(
            @Parameter(description = "ID do personal trainer", example = "1")
            @PathVariable Long personalId
    ) {

        return ResponseEntity.ok(
                alunoService.buscarPorPersonalId(personalId)
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar personal trainer",
            description = "Atualiza os dados de um personal trainer existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personal trainer atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personal trainer não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
    })
    public PersonalTrainerResponseDTO atualizar(
            @Parameter(description = "ID do personal trainer", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid PersonalTrainerRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        return personalTrainerService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir personal trainer",
            description = "Remove um personal trainer do sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Personal trainer removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personal trainer não encontrado")
    })
    public void deletar(
            @Parameter(description = "ID do personal trainer", example = "1")
            @PathVariable Long id
    ) throws RecursoNaoEncontradoException {

        personalTrainerService.deletar(id);
    }
}