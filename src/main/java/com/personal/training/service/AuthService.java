package com.personal.training.service;

import com.personal.training.dto.Login.LoginRequestDTO;
import com.personal.training.dto.Login.LoginResponseDTO;
import com.personal.training.dto.Login.TrocaSenhaDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.exception.SenhaIncorretaException;
import com.personal.training.model.Usuario;
import com.personal.training.repository.UsuarioRepository;
import com.personal.training.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO dto) throws RecursoNaoEncontradoException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        String token = jwtService.gerarToken(usuario);
        return new LoginResponseDTO(token, usuario.getTipo().name(), usuario.getId());
    }

    public void trocarSenha(Long id, TrocaSenhaDTO dto) throws SenhaIncorretaException, RecursoNaoEncontradoException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new SenhaIncorretaException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }
}