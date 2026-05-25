package com.personal.training.controller;

import com.personal.training.dto.AlunoRequestDTO;
import com.personal.training.dto.AlunoResponseDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponseDTO criar(
            @RequestBody @Valid AlunoRequestDTO dto
    ) throws RegraNegocioException {

        return alunoService.criar(dto);
    }

    @GetMapping
    public List<AlunoResponseDTO> listarTodos() {

        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    public AlunoResponseDTO buscarPorId(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return alunoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AlunoResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AlunoRequestDTO dto
    ) throws RegraNegocioException {

        return alunoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long id
    ) throws RegraNegocioException {

        alunoService.deletar(id);
    }
}