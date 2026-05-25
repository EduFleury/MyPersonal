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
public class Aluno{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double peso;

    private Double altura;

    private String objetivo;

    @OneToOne
    private Usuario usuario;

    @ManyToOne
    private PersonalTrainer personal;
}
