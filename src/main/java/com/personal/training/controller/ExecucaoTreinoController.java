package com.personal.training.controller;

import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoRequestDTO;
import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoResponseDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.ExecucaoTreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/execucoes-treino")
@RequiredArgsConstructor
public class ExecucaoTreinoController {

    private final ExecucaoTreinoService execucaoTreinoService;

    @PostMapping
    public ExecucaoTreinoResponseDTO criar(
            @RequestBody @Valid ExecucaoTreinoRequestDTO dto
    ) throws RegraNegocioException {

        return execucaoTreinoService.criar(dto);
    }

    @GetMapping
    public List<ExecucaoTreinoResponseDTO> listar() {

        return execucaoTreinoService.listar();
    }

    @GetMapping("/{id}")
    public ExecucaoTreinoResponseDTO buscarPorId(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return execucaoTreinoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ExecucaoTreinoResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ExecucaoTreinoRequestDTO dto
    ) throws RegraNegocioException {

        return execucaoTreinoService.atualizar(id, dto);
    }

    @PutMapping("/{id}/concluir")
    public ExecucaoTreinoResponseDTO concluir(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return execucaoTreinoService.concluir(id);
    }

    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id
    ) {

        execucaoTreinoService.excluir(id);
    }
}