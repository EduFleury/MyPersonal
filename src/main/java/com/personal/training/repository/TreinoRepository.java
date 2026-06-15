package com.personal.training.repository;

import com.personal.training.model.Treino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreinoRepository extends JpaRepository<Treino, Long> {

    List<Treino> findByAlunoId(Long alunoId);

    List<Treino> findByAlunoUsuarioEmail(String email);

    List<Treino> findByAlunoPersonalId(Long personalId);

    List<Treino> findByAlunoPersonalUsuarioEmail(String email);

}
