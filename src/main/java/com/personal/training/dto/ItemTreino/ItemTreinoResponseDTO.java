package com.personal.training.dto.ItemTreino;

public record ItemTreinoResponseDTO(

        Long id,

        String series,

        String repeticoes,

        String carga,

        Integer descansoSegundos,

        Long treinoId,

        String nomeTreino,

        Long exercicioId,

        String nomeExercicio

) {
}