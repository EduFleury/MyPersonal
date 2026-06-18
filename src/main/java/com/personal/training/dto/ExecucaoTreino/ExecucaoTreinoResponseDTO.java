package com.personal.training.dto.ExecucaoTreino;

import java.time.LocalDateTime;

public record ExecucaoTreinoResponseDTO(

        Long id,

        LocalDateTime dataInicio,

        LocalDateTime dataFim,

        String observacoes,

        Boolean concluido,

        Long alunoId,

        String nomeAluno,

        Long treinoId,

        String nomeTreino

) {
}