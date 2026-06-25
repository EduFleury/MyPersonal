package com.personal.training.repository;

import com.personal.training.model.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByUsuarioId(Long usuarioId);

    Optional<Aluno> findByUsuarioEmail(String email);

    List<Aluno> findByPersonalId(Long personalId);

    List<Aluno> findByPersonalUsuarioEmail(String email);

    Page<Aluno> findByPersonalUsuarioEmail(String email, Pageable pageable);

}
