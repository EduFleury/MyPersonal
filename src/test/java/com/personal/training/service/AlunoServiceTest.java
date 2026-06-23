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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonalTrainerRepository personalTrainerRepository;

    private Aluno criarAlunoMock(Long id, String objetivo) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Aluno Teste");
        usuario.setEmail("aluno@email.com");
        usuario.setTipo(TipoUsuario.ALUNO);

        Usuario usuarioPersonal = new Usuario();
        usuarioPersonal.setId(2L);
        usuarioPersonal.setNome("Personal Teste");
        usuarioPersonal.setTipo(TipoUsuario.PERSONAL);

        PersonalTrainer personal = new PersonalTrainer();
        personal.setId(2L);
        personal.setUsuario(usuarioPersonal);

        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setPeso(80.0);
        aluno.setAltura(1.75);
        aluno.setObjetivo(objetivo);
        aluno.setUsuario(usuario);
        aluno.setPersonal(personal);

        return aluno;
    }

    @Test
    void deveCriarAlunoComSucesso() throws Exception {

        AlunoRequestDTO dto = new AlunoRequestDTO(
                80.0,
                1.75,
                "Ganho de massa Magra",
                1L,
                2L
        );

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTipo(TipoUsuario.ALUNO);

        Usuario usuarioPersonal = new Usuario();
        usuarioPersonal.setId(2L);
        usuarioPersonal.setNome("Personal Teste");
        usuarioPersonal.setTipo(TipoUsuario.PERSONAL);

        PersonalTrainer personal = new PersonalTrainer();
        personal.setId(2L);
        personal.setUsuario(usuarioPersonal);

        Aluno alunoSalvo = new Aluno();
        alunoSalvo.setId(10L);
        alunoSalvo.setPeso(80.0);
        alunoSalvo.setAltura(1.75);
        alunoSalvo.setObjetivo("Ganho de massa Magra");
        alunoSalvo.setUsuario(usuario);
        alunoSalvo.setPersonal(personal);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(personalTrainerRepository.findById(2L)).thenReturn(Optional.of(personal));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoSalvo);

        AlunoResponseDTO response = alunoService.criar(dto);

        assertEquals(10L, response.id());
        assertEquals("Ganho de massa Magra", response.objetivo());
    }

    @Test
    void deveLancarErroSeUsuarioForPersonal() {

        AlunoRequestDTO dto = new AlunoRequestDTO(
                80.0,
                1.75,
                "Ganho de massa Magra",
                1L,
                2L
        );

        Usuario usuarioAlunoErro = new Usuario();
        usuarioAlunoErro.setId(1L);
        usuarioAlunoErro.setNome("Aluno Teste");
        usuarioAlunoErro.setTipo(TipoUsuario.PERSONAL);

        Usuario usuarioPersonal = new Usuario();
        usuarioPersonal.setId(2L);
        usuarioPersonal.setNome("Personal Teste");
        usuarioPersonal.setTipo(TipoUsuario.PERSONAL);

        PersonalTrainer personal = new PersonalTrainer();
        personal.setId(2L);
        personal.setUsuario(usuarioPersonal);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioAlunoErro));

        when(personalTrainerRepository.findById(2L))
                .thenReturn(Optional.of(personal));

        assertThrows(RegraNegocioException.class,
                () -> alunoService.criar(dto));
    }

    @Test
    void deveListarTodosOsAlunos() {
        Aluno aluno = criarAlunoMock(10L, "Objetivo Teste");
        when(alunoRepository.findAll()).thenReturn(List.of(aluno));

        List<AlunoResponseDTO> resultado = alunoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Objetivo Teste", resultado.get(0).objetivo());
    }

    @Test
    void deveBuscarAlunoPorIdComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        Aluno aluno = criarAlunoMock(id, "Hipertrofia");
        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        AlunoResponseDTO response = alunoService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        Long id = 99L;
        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> alunoService.buscarPorId(id));
    }

    @Test
    void deveBuscarAlunoPorIdUsuarioComSucesso() throws RecursoNaoEncontradoException {
        Long usuarioId = 1L;
        Aluno aluno = criarAlunoMock(10L, "Emagrecimento");
        when(alunoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(aluno));

        AlunoResponseDTO response = alunoService.buscarPorIdUsuario(usuarioId);

        assertNotNull(response);
        assertEquals(10L, response.id());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdUsuarioInexistente() {
        Long usuarioId = 99L;
        when(alunoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> alunoService.buscarPorIdUsuario(usuarioId));
    }

    @Test
    void deveBuscarAlunoPorEmailUsuarioComSucesso() throws RecursoNaoEncontradoException {
        UsuarioRequestFindByEmailDTO dto = new UsuarioRequestFindByEmailDTO("aluno@email.com");
        Aluno aluno = criarAlunoMock(10L, "Condicionamento");
        when(alunoRepository.findByUsuarioEmail(dto.email())).thenReturn(Optional.of(aluno));

        AlunoResponseDTO response = alunoService.buscarPorEmailUsuario(dto);

        assertNotNull(response);
        assertEquals("aluno@email.com", response.emailUsuario());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorEmailUsuarioInexistente() {
        UsuarioRequestFindByEmailDTO dto = new UsuarioRequestFindByEmailDTO("naoexiste@email.com");
        when(alunoRepository.findByUsuarioEmail(dto.email())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> alunoService.buscarPorEmailUsuario(dto));
    }

    @Test
    void deveBuscarAlunosPorPersonalId() {
        Long personalId = 2L;
        Aluno aluno = criarAlunoMock(10L, "Foco");
        when(alunoRepository.findByPersonalId(personalId)).thenReturn(List.of(aluno));

        List<AlunoResponseDTO> resultado = alunoService.buscarPorPersonalId(personalId);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(2L, resultado.get(0).personalId());
    }

    @Test
    void deveListarMeusAlunosPorEmailDoPersonal() {
        String emailPersonal = "personal@email.com";
        Aluno aluno = criarAlunoMock(10L, "Foco");
        when(alunoRepository.findByPersonalUsuarioEmail(emailPersonal)).thenReturn(List.of(aluno));

        List<AlunoResponseDTO> resultado = alunoService.listarMeusAlunos(emailPersonal);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void deveAtualizarAlunoComSucesso() throws RecursoNaoEncontradoException {
        Long alunoId = 10L;
        AlunoRequestDTO dto = new AlunoRequestDTO(85.0, 1.76, "Novo Objetivo", 1L, 2L);

        Aluno alunoExistente = criarAlunoMock(alunoId, "Objetivo Antigo");

        Usuario usuario = alunoExistente.getUsuario();
        PersonalTrainer personal = alunoExistente.getPersonal();

        Aluno alunoAtualizado = new Aluno();
        alunoAtualizado.setId(alunoId);
        alunoAtualizado.setPeso(dto.peso());
        alunoAtualizado.setAltura(dto.altura());
        alunoAtualizado.setObjetivo(dto.objetivo());
        alunoAtualizado.setUsuario(usuario);
        alunoAtualizado.setPersonal(personal);

        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(alunoExistente));
        when(usuarioRepository.findById(dto.usuarioId())).thenReturn(Optional.of(usuario));
        when(personalTrainerRepository.findById(dto.personalId())).thenReturn(Optional.of(personal));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoAtualizado);

        AlunoResponseDTO response = alunoService.atualizar(alunoId, dto);

        assertNotNull(response);
        assertEquals("Novo Objetivo", response.objetivo());
        assertEquals(85.0, response.peso());
    }

    @Test
    void deveLancarExcecaoAoAtualizarAlunoInexistente() {
        Long alunoId = 99L;
        AlunoRequestDTO dto = new AlunoRequestDTO(85.0, 1.76, "Novo Objetivo", 1L, 2L);

        when(alunoRepository.findById(alunoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> alunoService.atualizar(alunoId, dto));
    }

    @Test
    void deveDeletarAlunoComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        Aluno aluno = criarAlunoMock(id, "Objetivo");
        when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));

        alunoService.deletar(id);

        verify(alunoRepository, times(1)).delete(aluno);
    }

    @Test
    void deveLancarExcecaoAoDeletarAlunoInexistente() {
        Long id = 99L;
        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> alunoService.deletar(id));
    }

}
