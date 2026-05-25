package com.personal.training.controller;

import com.personal.training.dto.PersonalTrainerRequestDTO;
import com.personal.training.dto.PersonalTrainerResponseDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.PersonalTrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personais")
@RequiredArgsConstructor
public class PersonalTrainerController {

    private final PersonalTrainerService personalTrainerService;

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