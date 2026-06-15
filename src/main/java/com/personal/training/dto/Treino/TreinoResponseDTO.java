package com.personal.training.dto.Treino;

public record TreinoResponseDTO(
        Long id,
        String nome,
        String observacoes,
        Long alunoId
) {
}