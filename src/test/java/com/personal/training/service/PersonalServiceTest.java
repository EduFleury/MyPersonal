package com.personal.training.service;

import com.personal.training.dto.Personal.PersonalTrainerRequestDTO;
import com.personal.training.dto.Personal.PersonalTrainerResponseDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.model.Enum.TipoUsuario;
import com.personal.training.model.PersonalTrainer;
import com.personal.training.model.Usuario;
import com.personal.training.repository.PersonalTrainerRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalTrainerServiceTest {

    @InjectMocks
    private PersonalTrainerService personalTrainerService;

    @Mock
    private PersonalTrainerRepository personalTrainerRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void deveCriarPersonalComSucesso() throws Exception {
        PersonalTrainerRequestDTO dto = new PersonalTrainerRequestDTO("62999999999", 1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTipo(TipoUsuario.PERSONAL);

        PersonalTrainer personalSalvo = new PersonalTrainer();
        personalSalvo.setId(10L);
        personalSalvo.setTelefone("62999999999");
        personalSalvo.setUsuario(usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(personalTrainerRepository.save(any(PersonalTrainer.class))).thenReturn(personalSalvo);

        PersonalTrainerResponseDTO response = personalTrainerService.criar(dto);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("62999999999", response.telefone());
    }

    @Test
    void deveLancarErroAoCriarSeUsuarioForAluno() {
        PersonalTrainerRequestDTO dto = new PersonalTrainerRequestDTO("62999999999", 1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTipo(TipoUsuario.ALUNO);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThrows(RegraNegocioException.class, () -> personalTrainerService.criar(dto));
        verify(personalTrainerRepository, times(0)).save(any());
    }

    @Test
    void deveLancarErroAoCriarSeUsuarioNaoExistir() {
        PersonalTrainerRequestDTO dto = new PersonalTrainerRequestDTO("62999999999", 99L);
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> personalTrainerService.criar(dto));
    }

    @Test
    void deveListarTodosOsPersonals() {
        PersonalTrainer personal = criarPersonalMock(10L, "62999999999");

        Pageable pageable = PageRequest.of(0, 10);
        Page<PersonalTrainer> paginaDePersonal = new PageImpl<>(List.of(personal));
        when(personalTrainerRepository.findAll(pageable)).thenReturn(paginaDePersonal);


        Page<PersonalTrainerResponseDTO> resultado = personalTrainerService.listarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals("62999999999", resultado.getContent().get(0).telefone());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPersonals() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<PersonalTrainer> paginaDePersonalVazia = new PageImpl<>(Collections.emptyList());

        when(personalTrainerRepository.findAll(pageable)).thenReturn(paginaDePersonalVazia);

        Page<PersonalTrainerResponseDTO> resultado = personalTrainerService.listarTodos(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarPersonalPorIdComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        PersonalTrainer personal = criarPersonalMock(id, "62999999999");
        when(personalTrainerRepository.findById(id)).thenReturn(Optional.of(personal));

        PersonalTrainerResponseDTO response = personalTrainerService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        Long id = 99L;
        when(personalTrainerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> personalTrainerService.buscarPorId(id));
    }

    @Test
    void deveBuscarPersonalPorIdUsuarioComSucesso() throws RecursoNaoEncontradoException {
        Long usuarioId = 1L;
        PersonalTrainer personal = criarPersonalMock(10L, "62999999999");
        when(personalTrainerRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(personal));

        PersonalTrainerResponseDTO response = personalTrainerService.buscarPorIdUsuario(usuarioId);

        assertNotNull(response);
        assertEquals(1L, response.usuarioId());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdUsuarioInexistente() {
        Long usuarioId = 99L;
        when(personalTrainerRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> personalTrainerService.buscarPorIdUsuario(usuarioId));
    }

    @Test
    void deveBuscarPersonalPorEmailComSucesso() throws RecursoNaoEncontradoException {
        UsuarioRequestFindByEmailDTO dto = new UsuarioRequestFindByEmailDTO("personal@email.com");
        PersonalTrainer personal = criarPersonalMock(10L, "62999999999");
        when(personalTrainerRepository.findByUsuarioEmail(dto.email())).thenReturn(Optional.of(personal));

        PersonalTrainerResponseDTO response = personalTrainerService.buscarPorEmail(dto);

        assertNotNull(response);
        assertEquals("personal@email.com", response.emailUsuario());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorEmailInexistente() {
        UsuarioRequestFindByEmailDTO dto = new UsuarioRequestFindByEmailDTO("naoexiste@email.com");
        when(personalTrainerRepository.findByUsuarioEmail(dto.email())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> personalTrainerService.buscarPorEmail(dto));
    }

    @Test
    void deveAtualizarPersonalComSucesso() throws RecursoNaoEncontradoException {
        Long personalId = 10L;
        PersonalTrainerRequestDTO dto = new PersonalTrainerRequestDTO("62888888888", 1L);

        PersonalTrainer personalExistente = criarPersonalMock(personalId, "62999999999");
        Usuario usuario = personalExistente.getUsuario();

        PersonalTrainer personalAtualizado = new PersonalTrainer();
        personalAtualizado.setId(personalId);
        personalAtualizado.setTelefone(dto.telefone());
        personalAtualizado.setUsuario(usuario);

        when(personalTrainerRepository.findById(personalId)).thenReturn(Optional.of(personalExistente));
        when(usuarioRepository.findById(dto.usuarioId())).thenReturn(Optional.of(usuario));
        when(personalTrainerRepository.save(any(PersonalTrainer.class))).thenReturn(personalAtualizado);

        PersonalTrainerResponseDTO response = personalTrainerService.atualizar(personalId, dto);

        assertNotNull(response);
        assertEquals("62888888888", response.telefone());
    }

    @Test
    void deveLancarExcecaoAoAtualizarPersonalInexistente() {
        Long personalId = 99L;
        PersonalTrainerRequestDTO dto = new PersonalTrainerRequestDTO("62888888888", 1L);

        when(personalTrainerRepository.findById(personalId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> personalTrainerService.atualizar(personalId, dto));
    }

    @Test
    void deveDeletarPersonalComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        PersonalTrainer personal = criarPersonalMock(id, "62999999999");
        when(personalTrainerRepository.findById(id)).thenReturn(Optional.of(personal));

        personalTrainerService.deletar(id);

        verify(personalTrainerRepository, times(1)).delete(personal);
    }

    @Test
    void deveLancarExcecaoAoDeletarPersonalInexistente() {
        Long id = 99L;
        when(personalTrainerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> personalTrainerService.deletar(id));
    }

    private PersonalTrainer criarPersonalMock(Long id, String telefone) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Personal Teste");
        usuario.setEmail("personal@email.com");
        usuario.setTipo(TipoUsuario.PERSONAL);

        PersonalTrainer personalTrainer = new PersonalTrainer();
        personalTrainer.setId(id);
        personalTrainer.setTelefone(telefone);
        personalTrainer.setUsuario(usuario);

        return personalTrainer;
    }
}