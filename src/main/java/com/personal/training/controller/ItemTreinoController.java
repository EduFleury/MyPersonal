package com.personal.training.controller;

import com.personal.training.dto.ItemTreino.ItemTreinoRequestDTO;
import com.personal.training.dto.ItemTreino.ItemTreinoResponseDTO;
import com.personal.training.service.ItemTreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-treino")
@RequiredArgsConstructor
public class ItemTreinoController {

    private final ItemTreinoService itemTreinoService;

    @PostMapping
    public ItemTreinoResponseDTO criar(
            @RequestBody @Valid ItemTreinoRequestDTO dto
    ) {
        return itemTreinoService.criar(dto);
    }

    @GetMapping
    public List<ItemTreinoResponseDTO> listar() {
        return itemTreinoService.listar();
    }

    @GetMapping("/{id}")
    public ItemTreinoResponseDTO buscarPorId(
            @PathVariable Long id
    ) {
        return itemTreinoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ItemTreinoResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ItemTreinoRequestDTO dto
    ) {
        return itemTreinoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id
    ) {
        itemTreinoService.excluir(id);
    }
}