package com.personal.training.service;

import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoRequestDTO;
import com.personal.training.dto.ExecucaoTreino.ExecucaoTreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.model.Aluno;
import com.personal.training.model.Enum.TipoUsuario;
import com.personal.training.model.ExecucaoTreino;
import com.personal.training.model.Treino;
import com.personal.training.model.Usuario;
import com.personal.training.repository.AlunoRepository;
import com.personal.training.repository.ExecucaoTreinoRepository;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecucaoTreinoServiceTest {

    @InjectMocks
    private ExecucaoTreinoService execucaoTreinoService;

    @Mock
    private ExecucaoTreinoRepository execucaoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private TreinoRepository treinoRepository;

    @Test
    void deveCriarExecucaoTreinoConcluidaComSucesso() throws RecursoNaoEncontradoException {
        ExecucaoTreinoRequestDTO dto = new ExecucaoTreinoRequestDTO(1L, 2L, "Treino rendeu bem", true);
        Aluno aluno = criarAlunoMock(1L);
        Treino treino = criarTreinoMock(2L);

        ExecucaoTreino execucaoSalva = new ExecucaoTreino();
        execucaoSalva.setId(10L);
        execucaoSalva.setAluno(aluno);
        execucaoSalva.setTreino(treino);
        execucaoSalva.setObservacoes(dto.observacoes());
        execucaoSalva.setConcluido(true);
        execucaoSalva.setDataInicio(LocalDateTime.now());
        execucaoSalva.setDataFim(LocalDateTime.now());

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(treinoRepository.findById(2L)).thenReturn(Optional.of(treino));
        when(execucaoRepository.save(any(ExecucaoTreino.class))).thenReturn(execucaoSalva);

        ExecucaoTreinoResponseDTO response = execucaoTreinoService.criar(dto);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertTrue(response.concluido());
        assertNotNull(response.dataFim());
        assertEquals("Treino rendeu bem", response.observacoes());
    }

    @Test
    void deveCriarExecucaoTreinoNaoConcluidaComSucesso() throws RecursoNaoEncontradoException {
        ExecucaoTreinoRequestDTO dto = new ExecucaoTreinoRequestDTO(1L, 2L, "Iniciando agora", false);
        Aluno aluno = criarAlunoMock(1L);
        Treino treino = criarTreinoMock(2L);

        ExecucaoTreino execucaoSalva = new ExecucaoTreino();
        execucaoSalva.setId(10L);
        execucaoSalva.setAluno(aluno);
        execucaoSalva.setTreino(treino);
        execucaoSalva.setObservacoes(dto.observacoes());
        execucaoSalva.setConcluido(false);
        execucaoSalva.setDataInicio(LocalDateTime.now());
        execucaoSalva.setDataFim(null);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(treinoRepository.findById(2L)).thenReturn(Optional.of(treino));
        when(execucaoRepository.save(any(ExecucaoTreino.class))).thenReturn(execucaoSalva);

        ExecucaoTreinoResponseDTO response = execucaoTreinoService.criar(dto);

        assertNotNull(response);
        assertFalse(response.concluido());
        assertNull(response.dataFim());
    }

    @Test
    void deveLancarExcecaoAoCriarSeAlunoNaoExistir() {
        ExecucaoTreinoRequestDTO dto = new ExecucaoTreinoRequestDTO(99L, 2L, "Obs", false);
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> execucaoTreinoService.criar(dto));
        verify(execucaoRepository, times(0)).save(any());
    }

    @Test
    void deveLancarExcecaoAoCriarSeTreinoNaoExistir() {
        ExecucaoTreinoRequestDTO dto = new ExecucaoTreinoRequestDTO(1L, 99L, "Obs", false);
        Aluno aluno = criarAlunoMock(1L);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(treinoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> execucaoTreinoService.criar(dto));
        verify(execucaoRepository, times(0)).save(any());
    }

    @Test
    void deveListarTodasAsExecucoes() {
        ExecucaoTreino execucao = criarExecucaoMock(10L, 1L, 2L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ExecucaoTreino> paginaDeExecucoes = new PageImpl<>(List.of(execucao));
        when(execucaoRepository.findAll(pageable)).thenReturn(paginaDeExecucoes);

        Page<ExecucaoTreinoResponseDTO> resultado = execucaoTreinoService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals("Treino A", resultado.getContent().get(0).nomeTreino());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverExecucoes() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ExecucaoTreino> paginaDeExecucoes = new PageImpl<>(Collections.emptyList());

        when(execucaoRepository.findAll(pageable)).thenReturn(paginaDeExecucoes);

        Page<ExecucaoTreinoResponseDTO> resultado = execucaoTreinoService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveBuscarExecucaoPorIdComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        ExecucaoTreino execucao = criarExecucaoMock(id, 1L, 2L);
        when(execucaoRepository.findById(id)).thenReturn(Optional.of(execucao));

        ExecucaoTreinoResponseDTO response = execucaoTreinoService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        Long id = 99L;
        when(execucaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> execucaoTreinoService.buscarPorId(id));
    }

    @Test
    void deveAtualizarObservacoesComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        ExecucaoTreinoRequestDTO dto = new ExecucaoTreinoRequestDTO(1L, 2L, "Nova Observação", false);
        ExecucaoTreino execucaoExistente = criarExecucaoMock(id, 1L, 2L);

        ExecucaoTreino execucaoAtualizada = criarExecucaoMock(id, 1L, 2L);
        execucaoAtualizada.setObservacoes(dto.observacoes());

        when(execucaoRepository.findById(id)).thenReturn(Optional.of(execucaoExistente));
        when(execucaoRepository.save(any(ExecucaoTreino.class))).thenReturn(execucaoAtualizada);

        ExecucaoTreinoResponseDTO response = execucaoTreinoService.atualizar(id, dto);

        assertNotNull(response);
        assertEquals("Nova Observação", response.observacoes());
    }

    @Test
    void deveLancarExcecaoAoAtualizarInexistente() {
        Long id = 99L;
        ExecucaoTreinoRequestDTO dto = new ExecucaoTreinoRequestDTO(1L, 2L, "Obs", false);
        when(execucaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> execucaoTreinoService.atualizar(id, dto));
    }

    @Test
    void deveConcluirExecucaoComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        ExecucaoTreino execucaoExistente = criarExecucaoMock(id, 1L, 2L);
        execucaoExistente.setConcluido(false);

        ExecucaoTreino execucaoConcluida = criarExecucaoMock(id, 1L, 2L);
        execucaoConcluida.setConcluido(true);
        execucaoConcluida.setDataFim(LocalDateTime.now());

        when(execucaoRepository.findById(id)).thenReturn(Optional.of(execucaoExistente));
        when(execucaoRepository.save(any(ExecucaoTreino.class))).thenReturn(execucaoConcluida);

        ExecucaoTreinoResponseDTO response = execucaoTreinoService.concluir(id);

        assertNotNull(response);
        assertTrue(response.concluido());
        assertNotNull(response.dataFim());
    }

    @Test
    void deveLancarExcecaoAoConcluirInexistente() {
        Long id = 99L;
        when(execucaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> execucaoTreinoService.concluir(id));
    }

    @Test
    void deveExcluirExecucaoComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        ExecucaoTreino execucao = criarExecucaoMock(id, 1L, 2L);
        when(execucaoRepository.findById(id)).thenReturn(Optional.of(execucao));

        execucaoTreinoService.excluir(id);

        verify(execucaoRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirInexistente() {
        Long id = 99L;
        when(execucaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> execucaoTreinoService.excluir(id));
        verify(execucaoRepository, times(0)).deleteById(any());
    }

    // --- Métodos Auxiliares para construção de cenários ---

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

    private Treino criarTreinoMock(Long id) {
        Treino treino = new Treino();
        treino.setId(id);
        treino.setNome("Treino A");
        return treino;
    }

    private ExecucaoTreino criarExecucaoMock(Long id, Long alunoId, Long treinoId) {
        ExecucaoTreino execucao = new ExecucaoTreino();
        execucao.setId(id);
        execucao.setObservacoes("Observação padrão");
        execucao.setConcluido(false);
        execucao.setDataInicio(LocalDateTime.now());
        execucao.setAluno(criarAlunoMock(alunoId));
        execucao.setTreino(criarTreinoMock(treinoId));
        return execucao;
    }
}