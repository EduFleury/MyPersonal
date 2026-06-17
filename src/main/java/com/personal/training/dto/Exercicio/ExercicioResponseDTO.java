package com.personal.training.dto.Exercicio;

public record ExercicioResponseDTO(

        Long id,
        String nome,
        String grupoMuscular,
        String descricao

) {
}