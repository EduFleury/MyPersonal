package com.personal.training.controller;

import com.personal.training.dto.Exercicio.ExercicioRequestDTO;
import com.personal.training.dto.Exercicio.ExercicioResponseDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.ExercicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercicios")
@RequiredArgsConstructor
public class ExercicioController {

    private final ExercicioService exercicioService;

    @PostMapping
    public ExercicioResponseDTO criar(
            @RequestBody @Valid ExercicioRequestDTO dto
    ) {

        return exercicioService.criar(dto);
    }

    @GetMapping
    public List<ExercicioResponseDTO> listar() {

        return exercicioService.listar();
    }

    @GetMapping("/{id}")
    public ExercicioResponseDTO buscarPorId(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return exercicioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ExercicioResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ExercicioRequestDTO dto
    ) throws RegraNegocioException {

        return exercicioService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id
    ) throws RegraNegocioException {

        exercicioService.excluir(id);
    }
}