package com.personal.training.repository;

import com.personal.training.model.PersonalTrainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalTrainerRepository extends JpaRepository<PersonalTrainer, Long> {

    Optional<PersonalTrainer> findByUsuarioId(Long usuarioId);

    Optional<PersonalTrainer> findByUsuarioEmail(String email);


}
