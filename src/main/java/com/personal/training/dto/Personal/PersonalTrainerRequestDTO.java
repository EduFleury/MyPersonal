package com.personal.training.dto.Personal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonalTrainerRequestDTO(

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotNull(message = "Usuário é obrigatório")
        Long usuarioId

) {
}