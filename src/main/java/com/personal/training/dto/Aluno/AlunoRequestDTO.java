package com.personal.training.dto.Aluno;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlunoRequestDTO(

        @NotNull(message = "Peso é obrigatório")
        Double peso,

        @NotNull(message = "Altura é obrigatória")
        Double altura,

        @NotBlank(message = "Objetivo é obrigatório")
        String objetivo,

        @NotNull(message = "Usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "Personal é obrigatório")
        Long personalId

) {
}