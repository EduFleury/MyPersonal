package com.personal.training.controller;

import com.personal.training.dto.Treino.TreinoRequestDTO;
import com.personal.training.dto.Treino.TreinoResponseDTO;
import com.personal.training.service.TreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treinos")
@RequiredArgsConstructor
public class TreinoController {

    private final TreinoService treinoService;

    @PostMapping
    public TreinoResponseDTO criar(
            @RequestBody @Valid TreinoRequestDTO dto
    ) {
        return treinoService.criar(dto);
    }

    @GetMapping
    public List<TreinoResponseDTO> listar() {
        return treinoService.listar();
    }

    @GetMapping("/{id}")
    public TreinoResponseDTO buscarPorId(
            @PathVariable Long id
    ) {
        return treinoService.buscarPorId(id);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<TreinoResponseDTO> listarPorAluno(
            @PathVariable Long alunoId
    ) {
        return treinoService.listarPorAluno(alunoId);
    }

    @GetMapping("/personal/{personalId}")
    public List<TreinoResponseDTO> listarPorPersonal(
            @PathVariable Long personalId
    ) {
        return treinoService.listarPorPersonal(personalId);
    }

    @GetMapping("/personal/meus-treinos")
    public List<TreinoResponseDTO> listarPorPersonal(
            Authentication authentication
    ) {
        return treinoService.listarPorPersonalLogado(authentication.getName());
    }

    @PutMapping("/{id}")
    public TreinoResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TreinoRequestDTO dto
    ) {
        return treinoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id
    ) {
        treinoService.excluir(id);
    }

    @GetMapping("/meus-treinos")
    public List<TreinoResponseDTO> meusTreinos(
            Authentication authentication
    ) {

        return treinoService.listarMeusTreinos(
                authentication.getName()
        );
    }
}