package com.personal.training.dto.Treino;

public record TreinoRequestDTO(
        String nome,
        String observacoes,
        Long alunoId
) {
}