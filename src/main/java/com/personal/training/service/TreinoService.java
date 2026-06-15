package com.personal.training.service;


import com.personal.training.dto.Treino.TreinoRequestDTO;
import com.personal.training.dto.Treino.TreinoResponseDTO;
import com.personal.training.model.Aluno;
import com.personal.training.model.PersonalTrainer;
import com.personal.training.model.Treino;
import com.personal.training.model.Usuario;
import com.personal.training.repository.AlunoRepository;
import com.personal.training.repository.PersonalTrainerRepository;
import com.personal.training.repository.TreinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository;
    private final PersonalTrainerRepository personalTrainerRepository;

    public TreinoResponseDTO criar(TreinoRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Treino treino = new Treino();

        treino.setNome(dto.nome());
        treino.setObservacoes(dto.observacoes());
        treino.setAluno(aluno);

        treino = treinoRepository.save(treino);

        return toDTO(treino);
    }

    public TreinoResponseDTO buscarPorId(Long id) {

        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treino não encontrado"));

        return toDTO(treino);
    }

    public List<TreinoResponseDTO> listar() {

        return treinoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TreinoResponseDTO> listarPorAluno(Long alunoId) {

        return treinoRepository.findByAlunoId(alunoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TreinoResponseDTO> listarPorPersonal(Long personalId) {

        return treinoRepository.findByAlunoPersonalId(personalId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TreinoResponseDTO> listarMeusTreinos(String email) {

        return treinoRepository
                .findByAlunoUsuarioEmail(email)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TreinoResponseDTO atualizar(Long id, TreinoRequestDTO dto) {

        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treino não encontrado"));

        treino.setNome(dto.nome());
        treino.setObservacoes(dto.observacoes());

        treino = treinoRepository.save(treino);

        return toDTO(treino);
    }

    public void excluir(Long id) {

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

    public List<TreinoResponseDTO> listarPorPersonalLogado(String email) {

        Optional<PersonalTrainer> usuario = personalTrainerRepository.findByUsuarioEmail(email);

        if(!usuario.isEmpty()){

            Usuario usuario1 = usuario.get().getUsuario();

            return treinoRepository.findByAlunoPersonalId(usuario1.getId())
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }

        return List.of();
    }
}