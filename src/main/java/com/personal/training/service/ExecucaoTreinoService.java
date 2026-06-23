package com.personal.training.service;

import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoRequestDTO;
import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.model.Aluno;
import com.personal.training.model.ExecucaoTreino;
import com.personal.training.model.Treino;
import com.personal.training.repository.AlunoRepository;
import com.personal.training.repository.ExecucaoTreinoRepository;
import com.personal.training.repository.TreinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecucaoTreinoService {

    private final ExecucaoTreinoRepository execucaoRepository;
    private final AlunoRepository alunoRepository;
    private final TreinoRepository treinoRepository;

    public ExecucaoTreinoResponseDTO criar(
            ExecucaoTreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

        Treino treino = treinoRepository.findById(dto.treinoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Treino não encontrado"));

        ExecucaoTreino execucao = new ExecucaoTreino();

        execucao.setAluno(aluno);
        execucao.setTreino(treino);
        execucao.setObservacoes(dto.observacoes());

        if (Boolean.TRUE.equals(dto.concluido())) {
            execucao.setConcluido(true);
            execucao.setDataFim(LocalDateTime.now());
        }else{
            execucao.setConcluido(false);
        }

        execucao.setDataInicio(LocalDateTime.now());

        execucao = execucaoRepository.save(execucao);

        return toDTO(execucao);
    }

    public List<ExecucaoTreinoResponseDTO> listar() {

        return execucaoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ExecucaoTreinoResponseDTO buscarPorId(Long id) throws RecursoNaoEncontradoException {

        ExecucaoTreino execucao = execucaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Execução não encontrada"));

        return toDTO(execucao);
    }

    public ExecucaoTreinoResponseDTO atualizar(
            Long id,
            ExecucaoTreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        ExecucaoTreino execucao = execucaoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Execução não encontrada"));

        execucao.setObservacoes(dto.observacoes());

        execucao = execucaoRepository.save(execucao);

        return toDTO(execucao);

    }

    public ExecucaoTreinoResponseDTO concluir(Long id)
            throws RecursoNaoEncontradoException {

        ExecucaoTreino execucao = execucaoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Execução não encontrada"));

        execucao.setConcluido(true);
        execucao.setDataFim(LocalDateTime.now());

        execucao = execucaoRepository.save(execucao);

        return toDTO(execucao);
    }

    public void excluir(Long id) throws RecursoNaoEncontradoException{

        ExecucaoTreino execucao = execucaoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Execução não encontrada"));

        execucaoRepository.deleteById(id);
    }

    private ExecucaoTreinoResponseDTO toDTO(
            ExecucaoTreino execucao
    ) {

        return new ExecucaoTreinoResponseDTO(
                execucao.getId(),
                execucao.getDataInicio(),
                execucao.getDataFim(),
                execucao.getObservacoes(),
                execucao.getConcluido(),
                execucao.getAluno().getId(),
                execucao.getAluno().getUsuario().getNome(),
                execucao.getTreino().getId(),
                execucao.getTreino().getNome()
        );
    }
}