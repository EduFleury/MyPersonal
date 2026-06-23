package com.personal.training.service;

import com.personal.training.dto.Exercicio.ExercicioRequestDTO;
import com.personal.training.dto.Exercicio.ExercicioResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.model.Exercicio;
import com.personal.training.repository.ExercicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;

    public ExercicioResponseDTO criar(ExercicioRequestDTO dto) {

        Exercicio exercicio = new Exercicio();

        exercicio.setNome(dto.nome());
        exercicio.setGrupoMuscular(dto.grupoMuscular());
        exercicio.setDescricao(dto.descricao());

        exercicio = exercicioRepository.save(exercicio);

        return toDTO(exercicio);
    }

    public List<ExercicioResponseDTO> listar() {

        return exercicioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ExercicioResponseDTO buscarPorId(Long id) throws RecursoNaoEncontradoException {

        Exercicio exercicio = exercicioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Exercício não encontrado"));

        return toDTO(exercicio);
    }

    public ExercicioResponseDTO atualizar(
            Long id,
            ExercicioRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        Exercicio exercicio = exercicioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Exercício não encontrado"));

        exercicio.setNome(dto.nome());
        exercicio.setGrupoMuscular(dto.grupoMuscular());
        exercicio.setDescricao(dto.descricao());

        exercicio = exercicioRepository.save(exercicio);

        return toDTO(exercicio);
    }

    public void excluir(Long id) throws RecursoNaoEncontradoException {

        Exercicio exercicio = exercicioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Exercício não encontrado"));

        exercicioRepository.delete(exercicio);
    }

    private ExercicioResponseDTO toDTO(Exercicio exercicio) {

        return new ExercicioResponseDTO(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getGrupoMuscular(),
                exercicio.getDescricao()
        );
    }
}