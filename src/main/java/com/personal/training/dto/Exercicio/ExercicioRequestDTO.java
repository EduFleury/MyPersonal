package com.personal.training.dto.Exercicio;

import jakarta.validation.constraints.NotBlank;

public record ExercicioRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Grupo muscular é obrigatório")
        String grupoMuscular,

        String descricao

) {
}