package com.personal.training.dto;

public record AlunoResponseDTO(

        Long id,
        Double peso,
        Double altura,
        String objetivo,

        Long usuarioId,
        String nomeUsuario,
        String emailUsuario,

        Long personalId,
        String nomePersonal

) {
}