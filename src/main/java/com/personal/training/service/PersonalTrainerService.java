package com.personal.training.service;

import com.personal.training.dto.Personal.PersonalTrainerRequestDTO;
import com.personal.training.dto.Personal.PersonalTrainerResponseDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.model.Enum.TipoUsuario;
import com.personal.training.model.PersonalTrainer;
import com.personal.training.model.Usuario;
import com.personal.training.repository.PersonalTrainerRepository;
import com.personal.training.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalTrainerService {

    private final PersonalTrainerRepository personalTrainerRepository;
    private final UsuarioRepository usuarioRepository;

    public PersonalTrainerResponseDTO criar(PersonalTrainerRequestDTO dto) throws RegraNegocioException {

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        if(usuario.getTipo().equals(TipoUsuario.ALUNO)){
            throw new RegraNegocioException("Usuário tipo ALUNO não pode ser um PERSONAL");
        }

        PersonalTrainer personalTrainer = new PersonalTrainer();

        personalTrainer.setTelefone(dto.telefone());
        personalTrainer.setUsuario(usuario);

        PersonalTrainer personalSalvo =
                personalTrainerRepository.save(personalTrainer);

        return converterParaDTO(personalSalvo);
    }

    public List<PersonalTrainerResponseDTO> listarTodos() {

        return personalTrainerRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public PersonalTrainerResponseDTO buscarPorId(Long id) throws RegraNegocioException {

        PersonalTrainer personalTrainer =
                personalTrainerRepository.findById(id)
                        .orElseThrow(() ->
                                new RegraNegocioException("Personal não encontrado"));

        return converterParaDTO(personalTrainer);
    }

    public PersonalTrainerResponseDTO buscarPorIdUsuario(Long id) throws RegraNegocioException {

        PersonalTrainer personalTrainer =
                personalTrainerRepository.findByUsuarioId(id)
                        .orElseThrow(() ->
                                new RegraNegocioException("Personal não encontrado"));

        return converterParaDTO(personalTrainer);
    }

    public PersonalTrainerResponseDTO buscarPorEmail(UsuarioRequestFindByEmailDTO dto) throws RegraNegocioException {

        PersonalTrainer personalTrainer =
                personalTrainerRepository.findByUsuarioEmail(dto.email())
                        .orElseThrow(() ->
                                new RegraNegocioException("Personal não encontrado"));

        return converterParaDTO(personalTrainer);
    }

    public PersonalTrainerResponseDTO atualizar(
            Long id,
            PersonalTrainerRequestDTO dto
    ) throws RegraNegocioException {

        PersonalTrainer personalTrainer =
                personalTrainerRepository.findById(id)
                        .orElseThrow(() ->
                                new RegraNegocioException("Personal não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() ->
                        new RegraNegocioException("Usuário não encontrado"));

        personalTrainer.setTelefone(dto.telefone());
        personalTrainer.setUsuario(usuario);

        PersonalTrainer personalAtualizado =
                personalTrainerRepository.save(personalTrainer);

        return converterParaDTO(personalAtualizado);
    }

    public void deletar(Long id) throws RegraNegocioException {

        PersonalTrainer personalTrainer =
                personalTrainerRepository.findById(id)
                        .orElseThrow(() ->
                                new RegraNegocioException("Personal não encontrado"));

        personalTrainerRepository.delete(personalTrainer);
    }

    private PersonalTrainerResponseDTO converterParaDTO(
            PersonalTrainer personalTrainer
    ) {

        return new PersonalTrainerResponseDTO(
                personalTrainer.getId(),
                personalTrainer.getTelefone(),
                personalTrainer.getUsuario().getId(),
                personalTrainer.getUsuario().getNome(),
                personalTrainer.getUsuario().getEmail()
        );
    }
}