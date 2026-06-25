package com.personal.training.service;

import com.personal.training.dto.Treino.TreinoRequestDTO;
import com.personal.training.dto.Treino.TreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.model.Aluno;
import com.personal.training.model.Enum.TipoUsuario;
import com.personal.training.model.PersonalTrainer;
import com.personal.training.model.Treino;
import com.personal.training.model.Usuario;
import com.personal.training.repository.AlunoRepository;
import com.personal.training.repository.PersonalTrainerRepository;
import com.personal.training.repository.TreinoRepository;
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
class TreinoServiceTest {

    @InjectMocks
    private TreinoService treinoService;

    @Mock
    private TreinoRepository treinoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private PersonalTrainerRepository personalTrainerRepository;

    @Test
    void deveCriarTreinoComSucesso() throws RecursoNaoEncontradoException {
        TreinoRequestDTO dto = new TreinoRequestDTO("Treino A - Hipertrofia", "Foco em progressão de carga", 1L);
        Aluno aluno = criarAlunoMock(1L);

        Treino treinoSalvo = new Treino();
        treinoSalvo.setId(10L);
        treinoSalvo.setNome(dto.nome());
        treinoSalvo.setObservacoes(dto.observacoes());
        treinoSalvo.setAluno(aluno);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(treinoRepository.save(any(Treino.class))).thenReturn(treinoSalvo);

        TreinoResponseDTO response = treinoService.criar(dto);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("Treino A - Hipertrofia", response.nome());
        assertEquals(1L, response.alunoId());
    }

    @Test
    void deveLancarExcecaoAoCriarTreinoComAlunoInexistente() {
        TreinoRequestDTO dto = new TreinoRequestDTO("Treino A", "Obs", 99L);
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> treinoService.criar(dto));
        verify(treinoRepository, times(0)).save(any());
    }

    @Test
    void deveBuscarTreinoPorIdComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        Treino treino = criarTreinoMock(id, "Treino B", 1L);
        when(treinoRepository.findById(id)).thenReturn(Optional.of(treino));

        TreinoResponseDTO response = treinoService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("Treino B", response.nome());
    }

    @Test
    void deveLancarExcecaoAoBuscarTreinoPorIdInexistente() {
        Long id = 99L;
        when(treinoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> treinoService.buscarPorId(id));
    }

    @Test
    void deveListarTodosOsTreinos() {
        Pageable pageable = PageRequest.of(0, 10);
        Treino treino = criarTreinoMock(10L, "Treino C", 1L);
        Page<Treino> paginaDeTreinos = new PageImpl<>(List.of(treino));

        when(treinoRepository.findAll(pageable)).thenReturn(paginaDeTreinos);

        Page<TreinoResponseDTO> resultado = treinoService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
    }

    @Test
    void deveListarPorAluno() {
        Long alunoId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Treino treino = criarTreinoMock(10L, "Treino Aluno", alunoId);
        Page<Treino> paginaDeTreinos = new PageImpl<>(List.of(treino));

        when(treinoRepository.findByAlunoId(alunoId, pageable)).thenReturn(paginaDeTreinos);

        Page<TreinoResponseDTO> resultado = treinoService.listarPorAluno(alunoId, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(alunoId, resultado.getContent().get(0).alunoId());
    }

    @Test
    void deveListarPorPersonal() {
        Long personalId = 2L;
        Pageable pageable = PageRequest.of(0, 10);
        Treino treino = criarTreinoMock(10L, "Treino Personal", 1L);
        Page<Treino> paginaDeTreinos = new PageImpl<>(List.of(treino));

        when(treinoRepository.findByAlunoPersonalId(personalId, pageable)).thenReturn(paginaDeTreinos);

        Page<TreinoResponseDTO> resultado = treinoService.listarPorPersonal(personalId, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveListarMeusTreinosPorEmailDoAluno() throws RecursoNaoEncontradoException {
        String email = "aluno@email.com";
        Pageable pageable = PageRequest.of(0, 10);
        Treino treino = criarTreinoMock(10L, "Treino Meu", 1L);
        Page<Treino> paginaDeTreinos = new PageImpl<>(List.of(treino));

        when(treinoRepository.findByAlunoUsuarioEmail(email, pageable)).thenReturn(paginaDeTreinos);

        Page<TreinoResponseDTO> resultado = treinoService.listarMeusTreinos(email, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveAtualizarTreinoComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        TreinoRequestDTO dto = new TreinoRequestDTO("Treino Atualizado", "Nova Observação", 1L);

        Treino treinoExistente = criarTreinoMock(id, "Treino Antigo", 1L);
        Treino treinoAtualizado = criarTreinoMock(id, "Treino Atualizado", 1L);
        treinoAtualizado.setObservacoes("Nova Observação");

        when(treinoRepository.findById(id)).thenReturn(Optional.of(treinoExistente));
        when(treinoRepository.save(any(Treino.class))).thenReturn(treinoAtualizado);

        TreinoResponseDTO response = treinoService.atualizar(id, dto);

        assertNotNull(response);
        assertEquals("Treino Atualizado", response.nome());
        assertEquals("Nova Observação", response.observacoes());
    }

    @Test
    void deveLancarExcecaoAoAtualizarTreinoInexistente() {
        Long id = 99L;
        TreinoRequestDTO dto = new TreinoRequestDTO("Nome", "Obs", 1L);
        when(treinoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> treinoService.atualizar(id, dto));
    }

    @Test
    void deveExcluirTreinoComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        Treino treino = criarTreinoMock(id, "Treino Excluir", 1L);
        when(treinoRepository.findById(id)).thenReturn(Optional.of(treino));

        treinoService.excluir(id);

        verify(treinoRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirTreinoInexistente() {
        Long id = 99L;
        when(treinoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> treinoService.excluir(id));
        verify(treinoRepository, times(0)).deleteById(any());
    }

    @Test
    void deveListarPorPersonalLogadoComSucesso() throws RecursoNaoEncontradoException {
        String email = "personal@email.com";
        Pageable pageable = PageRequest.of(0, 10);

        Usuario usuarioPersonal = new Usuario();
        usuarioPersonal.setId(5L);
        usuarioPersonal.setEmail(email);

        PersonalTrainer personal = new PersonalTrainer();
        personal.setId(2L);
        personal.setUsuario(usuarioPersonal);

        Treino treino = criarTreinoMock(10L, "Treino Personal Logado", 1L);
        Page<Treino> paginaDeTreinos = new PageImpl<>(List.of(treino));

        when(personalTrainerRepository.findByUsuarioEmail(email)).thenReturn(Optional.of(personal));
        when(treinoRepository.findByAlunoPersonalId(2L, pageable)).thenReturn(paginaDeTreinos);

        Page<TreinoResponseDTO> resultado = treinoService.listarPorPersonalLogado(email, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(personalTrainerRepository, times(1)).findByUsuarioEmail(email);
    }

    @Test
    void deveLancarExcecaoAoListarPorPersonalLogadoSeEmailNaoExistir() {
        String email = "naoexiste@email.com";
        Pageable pageable = PageRequest.of(0, 10);
        when(personalTrainerRepository.findByUsuarioEmail(email)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> treinoService.listarPorPersonalLogado(email, pageable));
        verify(treinoRepository, times(0)).findByAlunoPersonalId(any(), any());
    }

    private Aluno criarAlunoMock(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Aluno Teste");
        usuario.setTipo(TipoUsuario.ALUNO);

        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setUsuario(usuario);
        return aluno;
    }

    private Treino criarTreinoMock(Long id, String nome, Long alunoId) {
        Treino treino = new Treino();
        treino.setId(id);
        treino.setNome(nome);
        treino.setObservacoes("Observação do treino de teste");
        treino.setAluno(criarAlunoMock(alunoId));
        return treino;
    }
}