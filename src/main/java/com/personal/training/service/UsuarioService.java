package com.personal.training.service;

import com.personal.training.dto.Usuario.UsuarioRequestDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.dto.Usuario.UsuarioResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.model.Usuario;
import com.personal.training.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) throws RecursoNaoEncontradoException {

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RecursoNaoEncontradoException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setTipo(dto.tipo());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaDTO(usuarioSalvo);
    }

    public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {

        return usuarioRepository.findAll(pageable)
                .map(this::converterParaDTO);
    }

    public UsuarioResponseDTO buscarPorId(Long id) throws RecursoNaoEncontradoException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        return converterParaDTO(usuario);
    }

    public UsuarioResponseDTO buscarPorEmail(UsuarioRequestFindByEmailDTO dto) throws RecursoNaoEncontradoException {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        return converterParaDTO(usuario);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) throws RecursoNaoEncontradoException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTipo(dto.tipo());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return converterParaDTO(usuarioAtualizado);
    }

    public void deletar(Long id) throws RecursoNaoEncontradoException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }

    private UsuarioResponseDTO converterParaDTO(Usuario usuario) {

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo()
        );
    }

}