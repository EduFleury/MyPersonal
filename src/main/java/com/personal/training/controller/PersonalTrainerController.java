package com.personal.training.controller;

import com.personal.training.dto.Aluno.AlunoResponseDTO;
import com.personal.training.dto.Personal.PersonalTrainerRequestDTO;
import com.personal.training.dto.Personal.PersonalTrainerResponseDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AlunoService;
import com.personal.training.service.PersonalTrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personais")
@RequiredArgsConstructor
public class PersonalTrainerController {

    private final PersonalTrainerService personalTrainerService;
    private final AlunoService alunoService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonalTrainerResponseDTO criar(
            @RequestBody @Valid PersonalTrainerRequestDTO dto
    ) throws RegraNegocioException {

        return personalTrainerService.criar(dto);
    }

    @GetMapping
    public List<PersonalTrainerResponseDTO> listarTodos() {

        return personalTrainerService.listarTodos();
    }

    @GetMapping("/{id}")
    public PersonalTrainerResponseDTO buscarPorId(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return personalTrainerService.buscarPorId(id);
    }

    @GetMapping("/usuario/{id}")
    public PersonalTrainerResponseDTO buscarPorIdUsuario(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return personalTrainerService.buscarPorIdUsuario(id);
    }

    @GetMapping("/email")
    public PersonalTrainerResponseDTO buscarPorEmail(
            @RequestBody @Valid UsuarioRequestFindByEmailDTO dto
    ) throws RegraNegocioException {

        return personalTrainerService.buscarPorEmail(dto);
    }

    @GetMapping("/alunos/{personalId}")
    public ResponseEntity<List<AlunoResponseDTO>> buscarAlunosPorPersonalId(
            @PathVariable Long personalId
    ) {

        return ResponseEntity.ok(
                alunoService.buscarPorPersonalId(personalId)
        );
    }

    @PutMapping("/{id}")
    public PersonalTrainerResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PersonalTrainerRequestDTO dto
    ) throws RegraNegocioException {

        return personalTrainerService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long id
    ) throws RegraNegocioException {

        personalTrainerService.deletar(id);
    }
}