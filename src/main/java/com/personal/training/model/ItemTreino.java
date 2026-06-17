package com.personal.training.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ItemTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String series;

    private String repeticoes;

    private String carga;

    private Integer descansoSegundos;

    @ManyToOne
    private Treino treino;

    @ManyToOne
    private Exercicio exercicio;
}