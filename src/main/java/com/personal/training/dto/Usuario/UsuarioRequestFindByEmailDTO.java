package com.personal.training.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestFindByEmailDTO(

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email
) {
}
