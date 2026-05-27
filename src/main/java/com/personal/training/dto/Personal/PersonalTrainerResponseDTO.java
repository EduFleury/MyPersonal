package com.personal.training.dto.Personal;

public record PersonalTrainerResponseDTO(

        Long id,
        String telefone,
        Long usuarioId,
        String nomeUsuario,
        String emailUsuario

) {
}