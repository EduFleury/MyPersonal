package com.personal.training.repository;

import com.personal.training.model.ExecucaoTreino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecucaoTreinoRepository extends JpaRepository<ExecucaoTreino, Long> {
}
