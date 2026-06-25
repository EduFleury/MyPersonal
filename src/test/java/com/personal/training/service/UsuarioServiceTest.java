package com.personal.training.service;

import com.personal.training.dto.Usuario.UsuarioRequestDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.dto.Usuario.UsuarioResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.model.Aluno;
import com.personal.training.model.Enum.TipoUsuario;
import com.personal.training.model.Usuario;
import com.personal.training.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {

        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Eduardo",
                "edu@email.com",
                "123",
                TipoUsuario.ALUNO
        );

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Eduardo");
        usuarioSalvo.setEmail("edu@email.com");
        usuarioSalvo.setSenha("criptografada");
        usuarioSalvo.setTipo(TipoUsuario.ALUNO);

        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.senha())).thenReturn("criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioResponseDTO response = usuarioService.criar(dto);

        assertEquals("Eduardo", response.nome());
        assertEquals("edu@email.com", response.email());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {

        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Eduardo",
                "edu@email.com",
                "123",
                TipoUsuario.ALUNO
        );

        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> usuarioService.criar(dto));
    }

    @Test
    void deveListarTodosOsUsuarios() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Eduardo");
        usuario.setEmail("edu@email.com");
        usuario.setTipo(TipoUsuario.ALUNO);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> paginaDeUsuarios = new PageImpl<>(List.of(usuario));
        when(usuarioRepository.findAll(pageable)).thenReturn(paginaDeUsuarios);

        Page<UsuarioResponseDTO> resultado = usuarioService.listarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        assertEquals("Eduardo", resultado.getContent().get(0).nome());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverUsuarios() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> paginaDeUsuariosVazia = new PageImpl<>(Collections.emptyList());

        when(usuarioRepository.findAll(pageable)).thenReturn(paginaDeUsuariosVazia);

        Page<UsuarioResponseDTO> resultado = usuarioService.listarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() throws RecursoNaoEncontradoException {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Eduardo");
        usuario.setEmail("edu@email.com");
        usuario.setTipo(TipoUsuario.ALUNO);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = usuarioService.buscarPorId(id);

        assertEquals(id, response.id());
        assertEquals("Eduardo", response.nome());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        Long id = 99L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> usuarioService.buscarPorId(id));
    }

    @Test
    void deveBuscarUsuarioPorEmailComSucesso() throws RecursoNaoEncontradoException {
        UsuarioRequestFindByEmailDTO dto = new UsuarioRequestFindByEmailDTO("edu@email.com");
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Eduardo");
        usuario.setEmail("edu@email.com");
        usuario.setTipo(TipoUsuario.ALUNO);

        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = usuarioService.buscarPorEmail(dto);

        assertEquals("edu@email.com", response.email());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorEmailInexistente() {
        UsuarioRequestFindByEmailDTO dto = new UsuarioRequestFindByEmailDTO("inexistente@email.com");
        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> usuarioService.buscarPorEmail(dto));
    }

    @Test
    void deveAtualizarUsuarioComSucessoAlterandoSenha() throws RecursoNaoEncontradoException {
        Long id = 1L;
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Eduardo Atualizado", "novo@email.com", "novaSenha", TipoUsuario.ALUNO);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Eduardo");
        usuarioExistente.setEmail("edu@email.com");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(id);
        usuarioAtualizado.setNome("Eduardo Atualizado");
        usuarioAtualizado.setEmail("novo@email.com");
        usuarioAtualizado.setTipo(TipoUsuario.ALUNO);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.encode(dto.senha())).thenReturn("senhaCriptografadaNova");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        UsuarioResponseDTO response = usuarioService.atualizar(id, dto);

        assertEquals("Eduardo Atualizado", response.nome());
        assertEquals("novo@email.com", response.email());
        verify(passwordEncoder, times(1)).encode("novaSenha");
    }

    @Test
    void deveAtualizarUsuarioSemAlterarSenhaQuandoSenhaForNula() throws RecursoNaoEncontradoException {
        Long id = 1L;
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Eduardo", "edu@email.com", null, TipoUsuario.ALUNO);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setSenha("senhaAntigaCriptografada");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        usuarioService.atualizar(id, dto);

        // Garante que o encoder não foi chamado porque a senha era nula
        verify(passwordEncoder, times(0)).encode(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        Long id = 99L;
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Nome", "email@email.com", "123", TipoUsuario.ALUNO);

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> usuarioService.atualizar(id, dto));
    }

    @Test
    void deveDeletarUsuarioComSucesso() throws RecursoNaoEncontradoException {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(id);

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        Long id = 99L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> usuarioService.deletar(id));
    }
}
