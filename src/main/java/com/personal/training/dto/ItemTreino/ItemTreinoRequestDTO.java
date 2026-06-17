package com.personal.training.dto.ItemTreino;

import jakarta.validation.constraints.NotNull;

public record ItemTreinoRequestDTO(

        @NotNull(message = "Séries é obrigatório")
        String series,

        @NotNull(message = "Repetições é obrigatório")
        String repeticoes,

        String carga,

        @NotNull(message = "Descanso é obrigatório")
        Integer descansoSegundos,

        @NotNull(message = "Treino é obrigatório")
        Long treinoId,

        @NotNull(message = "Exercício é obrigatório")
        Long exercicioId

) {
}