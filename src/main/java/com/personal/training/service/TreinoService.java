package com.personal.training.service;


import com.personal.training.dto.Treino.TreinoRequestDTO;
import com.personal.training.dto.Treino.TreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.model.Aluno;
import com.personal.training.model.PersonalTrainer;
import com.personal.training.model.Treino;
import com.personal.training.model.Usuario;
import com.personal.training.repository.AlunoRepository;
import com.personal.training.repository.PersonalTrainerRepository;
import com.personal.training.repository.TreinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository;
    private final PersonalTrainerRepository personalTrainerRepository;

    public TreinoResponseDTO criar(TreinoRequestDTO dto) throws RecursoNaoEncontradoException {

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

        Treino treino = new Treino();

        treino.setNome(dto.nome());
        treino.setObservacoes(dto.observacoes());
        treino.setAluno(aluno);

        treino = treinoRepository.save(treino);

        return toDTO(treino);
    }

    public TreinoResponseDTO buscarPorId(Long id) throws RecursoNaoEncontradoException {

        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Treino não encontrado"));

        return toDTO(treino);
    }

    public Page<TreinoResponseDTO> listar(Pageable pageable) {

        return treinoRepository.findAll(pageable)
                .map(this::toDTO);
    }

    public Page<TreinoResponseDTO> listarPorAluno(Long alunoId, Pageable pageable) {
        return treinoRepository.findByAlunoId(alunoId, pageable)
                .map(this::toDTO);
    }

    public Page<TreinoResponseDTO> listarPorPersonal(Long personalId, Pageable pageable) {
        return treinoRepository.findByAlunoPersonalId(personalId, pageable)
                .map(this::toDTO);
    }

    public Page<TreinoResponseDTO> listarMeusTreinos(String email, Pageable pageable) {
        return treinoRepository.findByAlunoUsuarioEmail(email, pageable)
                .map(this::toDTO);
    }

    public TreinoResponseDTO atualizar(Long id, TreinoRequestDTO dto) throws RecursoNaoEncontradoException {

        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Treino não encontrado"));

        treino.setNome(dto.nome());
        treino.setObservacoes(dto.observacoes());

        treino = treinoRepository.save(treino);

        return toDTO(treino);
    }

    public void excluir(Long id) throws RecursoNaoEncontradoException{

        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Treino não encontrado"));

        treinoRepository.deleteById(id);
    }

    private TreinoResponseDTO toDTO(Treino treino) {

        return new TreinoResponseDTO(
                treino.getId(),
                treino.getNome(),
                treino.getObservacoes(),
                treino.getAluno().getId()
        );
    }

    public Page<TreinoResponseDTO> listarPorPersonalLogado(String email, Pageable pageable) throws RecursoNaoEncontradoException {
        PersonalTrainer personal = personalTrainerRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personal Trainer não encontrado"));

        return treinoRepository.findByAlunoPersonalId(personal.getId(), pageable)
                .map(this::toDTO);
    }
}