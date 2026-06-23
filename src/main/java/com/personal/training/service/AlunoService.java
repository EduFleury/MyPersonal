package com.personal.training.service;

import com.personal.training.dto.Aluno.AlunoRequestDTO;
import com.personal.training.dto.Aluno.AlunoResponseDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.model.Aluno;
import com.personal.training.model.Enum.TipoUsuario;
import com.personal.training.model.PersonalTrainer;
import com.personal.training.model.Usuario;
import com.personal.training.repository.AlunoRepository;
import com.personal.training.repository.PersonalTrainerRepository;
import com.personal.training.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonalTrainerRepository personalTrainerRepository;

    public AlunoResponseDTO criar(AlunoRequestDTO dto) throws RegraNegocioException, RecursoNaoEncontradoException {

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado"));

        PersonalTrainer personal = personalTrainerRepository
                .findById(dto.personalId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Personal não encontrado"));

        if(usuario.getTipo().equals(TipoUsuario.PERSONAL)){
            throw new RegraNegocioException("Usuário do tipo PERSONAL não pode ser um ALUNO");
        }

        Aluno aluno = new Aluno();

        aluno.setPeso(dto.peso());
        aluno.setAltura(dto.altura());
        aluno.setObjetivo(dto.objetivo());
        aluno.setUsuario(usuario);
        aluno.setPersonal(personal);

        Aluno alunoSalvo = alunoRepository.save(aluno);

        return converterParaDTO(alunoSalvo);
    }

    public List<AlunoResponseDTO> listarTodos() {

        return alunoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public AlunoResponseDTO buscarPorId(Long id) throws RecursoNaoEncontradoException {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                         new RecursoNaoEncontradoException("Aluno não encontrado"));

        return converterParaDTO(aluno);
    }

    public AlunoResponseDTO buscarPorIdUsuario(Long id) throws RecursoNaoEncontradoException {

        Aluno aluno = alunoRepository.findByUsuarioId(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Aluno não encontrado"));

        return converterParaDTO(aluno);
    }

    public AlunoResponseDTO buscarPorEmailUsuario(UsuarioRequestFindByEmailDTO dto) throws RecursoNaoEncontradoException {

        Aluno aluno = alunoRepository.findByUsuarioEmail(dto.email())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Aluno não encontrado"));

        return converterParaDTO(aluno);
    }

    public List<AlunoResponseDTO> buscarPorPersonalId(Long personalId) {

        List<Aluno> alunos = alunoRepository.findByPersonalId(personalId);

        return alunos.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<AlunoResponseDTO> listarMeusAlunos(String email) {

        return alunoRepository
                .findByPersonalUsuarioEmail(email)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public AlunoResponseDTO atualizar(
            Long id,
            AlunoRequestDTO dto
    ) throws RecursoNaoEncontradoException {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Aluno não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado"));

        PersonalTrainer personal = personalTrainerRepository
                .findById(dto.personalId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Personal não encontrado"));

        aluno.setPeso(dto.peso());
        aluno.setAltura(dto.altura());
        aluno.setObjetivo(dto.objetivo());
        aluno.setUsuario(usuario);
        aluno.setPersonal(personal);

        Aluno alunoAtualizado = alunoRepository.save(aluno);

        return converterParaDTO(alunoAtualizado);
    }

    public void deletar(Long id) throws RecursoNaoEncontradoException {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Aluno não encontrado"));

        alunoRepository.delete(aluno);
    }

    private AlunoResponseDTO converterParaDTO(Aluno aluno) {

        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getPeso(),
                aluno.getAltura(),
                aluno.getObjetivo(),

                aluno.getUsuario().getId(),
                aluno.getUsuario().getNome(),
                aluno.getUsuario().getEmail(),

                aluno.getPersonal().getId(),
                aluno.getPersonal().getUsuario().getNome()
        );
    }
}