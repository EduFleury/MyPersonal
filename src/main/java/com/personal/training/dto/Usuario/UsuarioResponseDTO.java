package com.personal.training.dto.Usuario;

import com.personal.training.model.Enum.TipoUsuario;

public record UsuarioResponseDTO(

        Long id,
        String nome,
        String email,
        TipoUsuario tipo

) {
}