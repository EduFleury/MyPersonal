package com.personal.training.service;

import com.personal.training.dto.Login.LoginRequestDTO;
import com.personal.training.dto.Login.LoginResponseDTO;
import com.personal.training.dto.Login.TrocaSenhaDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.SenhaIncorretaException;
import com.personal.training.model.Enum.TipoUsuario;
import com.personal.training.model.Usuario;
import com.personal.training.repository.UsuarioRepository;
import com.personal.training.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void deveAutenticarELogarComSucesso() throws RecursoNaoEncontradoException {
        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("user@email.com");
        usuario.setTipo(TipoUsuario.ALUNO);

        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.of(usuario));
        when(jwtService.gerarToken(usuario)).thenReturn("token-jwt-mockado");

        LoginResponseDTO response = authService.login(dto);

        assertNotNull(response);
        assertEquals("token-jwt-mockado", response.token());
        assertEquals("ALUNO", response.tipo());
        assertEquals(1L, response.id());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void deveLancarExcecaoNoLoginSeUsuarioNaoExistir() {
        LoginRequestDTO dto = new LoginRequestDTO("naoexiste@email.com", "123456");
        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> authService.login(dto));
        verify(jwtService, times(0)).gerarToken(any());
    }

    @Test
    void deveTrocarSenhaComSucesso() throws RecursoNaoEncontradoException, SenhaIncorretaException {
        Long usuarioId = 1L;
        TrocaSenhaDTO dto = new TrocaSenhaDTO("senhaVelha", "senhaNova");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setSenha("senhaVelhaCriptografada");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())).thenReturn(true);
        when(passwordEncoder.encode(dto.novaSenha())).thenReturn("senhaNovaCriptografada");

        authService.trocarSenha(usuarioId, dto);

        assertEquals("senhaNovaCriptografada", usuario.getSenha());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void deveLancarExcecaoAoTrocarSenhaSeUsuarioNaoExistir() {
        Long usuarioId = 99L;
        TrocaSenhaDTO dto = new TrocaSenhaDTO("senhaVelha", "senhaNova");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> authService.trocarSenha(usuarioId, dto));
        verify(usuarioRepository, times(0)).save(any());
    }

    @Test
    void deveLancarExcecaoAoTrocarSenhaSeSenhaAtualIncorreta() {
        Long usuarioId = 1L;
        TrocaSenhaDTO dto = new TrocaSenhaDTO("senhaErrada", "senhaNova");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setSenha("senhaCertaCriptografada");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())).thenReturn(false);

        assertThrows(SenhaIncorretaException.class, () -> authService.trocarSenha(usuarioId, dto));
        verify(usuarioRepository, times(0)).save(any());
        verify(passwordEncoder, times(0)).encode(any());
    }
}