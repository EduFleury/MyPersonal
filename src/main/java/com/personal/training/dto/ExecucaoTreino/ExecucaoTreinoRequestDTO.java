package com.personal.training.dto.ExecucaoTreino;

import jakarta.validation.constraints.NotNull;

public record ExecucaoTreinoRequestDTO(

        @NotNull
        Long alunoId,

        @NotNull
        Long treinoId,

        String observacoes,

        Boolean concluido

) {
}