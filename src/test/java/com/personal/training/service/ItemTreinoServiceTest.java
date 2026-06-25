package com.personal.training.service;

import com.personal.training.dto.ItemTreino.ItemTreinoRequestDTO;
import com.personal.training.dto.ItemTreino.ItemTreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.model.Exercicio;
import com.personal.training.model.ItemTreino;
import com.personal.training.model.Treino;
import com.personal.training.model.Usuario;
import com.personal.training.repository.ExercicioRepository;
import com.personal.training.repository.ItemTreinoRepository;
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
class ItemTreinoServiceTest {

    @InjectMocks
    private ItemTreinoService itemTreinoService;

    @Mock
    private ItemTreinoRepository itemTreinoRepository;

    @Mock
    private TreinoRepository treinoRepository;

    @Mock
    private ExercicioRepository exercicioRepository;

    @Test
    void deveCriarItemTreinoComSucesso() throws RecursoNaoEncontradoException {
        ItemTreinoRequestDTO dto = new ItemTreinoRequestDTO("4", "10 a 12", "40kg", 60, 1L, 2L);
        Treino treino = criarTreinoMock(1L, "Treino A");
        Exercicio exercicio = criarExercicioMock(2L, "Supino");

        ItemTreino itemSalvo = new ItemTreino(10L, "4", "10 a 12", "40kg", 60, treino, exercicio);

        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));
        when(exercicioRepository.findById(2L)).thenReturn(Optional.of(exercicio));
        when(itemTreinoRepository.save(any(ItemTreino.class))).thenReturn(itemSalvo);

        ItemTreinoResponseDTO response = itemTreinoService.criar(dto);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("4", response.series());
        assertEquals("Treino A", response.nomeTreino());
        assertEquals("Supino", response.nomeExercicio());
    }

    @Test
    void deveLancarExcecaoAoCriarSeTreinoNaoExistir() {
        ItemTreinoRequestDTO dto = new ItemTreinoRequestDTO("4", "12", "20kg", 60, 99L, 2L);
        when(treinoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemTreinoService.criar(dto));
        verify(itemTreinoRepository, times(0)).save(any());
    }

    @Test
    void deveLancarExcecaoAoCriarSeExercicioNaoExistir() {
        ItemTreinoRequestDTO dto = new ItemTreinoRequestDTO("4", "12", "20kg", 60, 1L, 99L);
        Treino treino = criarTreinoMock(1L, "Treino A");

        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));
        when(exercicioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemTreinoService.criar(dto));
        verify(itemTreinoRepository, times(0)).save(any());
    }

    @Test
    void deveListarTodosOsItensTreino() {
        ItemTreino item = criarItemTreinoMock(10L, "3", "15", "10kg", 45, 1L, 2L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ItemTreino> paginaDeItensTreino = new PageImpl<>(List.of(item));
        when(itemTreinoRepository.findAll(pageable)).thenReturn(paginaDeItensTreino);

        Page<ItemTreinoResponseDTO> resultado = itemTreinoService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals("3", resultado.getContent().get(0).series());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverItens() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ItemTreino> paginaDeItensTreino = new PageImpl<>(Collections.emptyList());

        when(itemTreinoRepository.findAll(pageable)).thenReturn(paginaDeItensTreino);

        Page<ItemTreinoResponseDTO> resultado = itemTreinoService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveBuscarItemTreinoPorIdComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        ItemTreino item = criarItemTreinoMock(id, "4", "8", "50kg", 90, 1L, 2L);
        when(itemTreinoRepository.findById(id)).thenReturn(Optional.of(item));

        ItemTreinoResponseDTO response = itemTreinoService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("50kg", response.carga());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        Long id = 99L;
        when(itemTreinoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemTreinoService.buscarPorId(id));
    }

    @Test
    void deveAtualizarItemTreinoComSucesso() throws RecursoNaoEncontradoException {
        Long itemId = 10L;
        ItemTreinoRequestDTO dto = new ItemTreinoRequestDTO("5", "6", "60kg", 120, 1L, 2L);

        ItemTreino itemExistente = criarItemTreinoMock(itemId, "4", "8", "50kg", 90, 1L, 2L);
        Treino treino = itemExistente.getTreino();
        Exercicio exercicio = itemExistente.getExercicio();

        ItemTreino itemAtualizado = new ItemTreino(itemId, "5", "6", "60kg", 120, treino, exercicio);

        when(itemTreinoRepository.findById(itemId)).thenReturn(Optional.of(itemExistente));
        when(treinoRepository.findById(dto.treinoId())).thenReturn(Optional.of(treino));
        when(exercicioRepository.findById(dto.exercicioId())).thenReturn(Optional.of(exercicio));
        when(itemTreinoRepository.save(any(ItemTreino.class))).thenReturn(itemAtualizado);

        ItemTreinoResponseDTO response = itemTreinoService.atualizar(itemId, dto);

        assertNotNull(response);
        assertEquals("5", response.series());
        assertEquals("60kg", response.carga());
        assertEquals(120, response.descansoSegundos());
    }

    @Test
    void deveLancarExcecaoAoAtualizarItemInexistente() {
        Long itemId = 99L;
        ItemTreinoRequestDTO dto = new ItemTreinoRequestDTO("5", "6", "60kg", 120, 1L, 2L);
        when(itemTreinoRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemTreinoService.atualizar(itemId, dto));
    }

    @Test
    void deveLancarExcecaoAoAtualizarSeTreinoNaoExistir() {
        Long itemId = 10L;
        ItemTreinoRequestDTO dto = new ItemTreinoRequestDTO("5", "6", "60kg", 120, 99L, 2L);
        ItemTreino itemExistente = criarItemTreinoMock(itemId, "4", "8", "50kg", 90, 1L, 2L);

        when(itemTreinoRepository.findById(itemId)).thenReturn(Optional.of(itemExistente));
        when(treinoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemTreinoService.atualizar(itemId, dto));
    }

    @Test
    void deveLancarExcecaoAoAtualizarSeExercicioNaoExistir() {
        Long itemId = 10L;
        ItemTreinoRequestDTO dto = new ItemTreinoRequestDTO("5", "6", "60kg", 120, 1L, 99L);
        ItemTreino itemExistente = criarItemTreinoMock(itemId, "4", "8", "50kg", 90, 1L, 2L);
        Treino treino = itemExistente.getTreino();

        when(itemTreinoRepository.findById(itemId)).thenReturn(Optional.of(itemExistente));
        when(treinoRepository.findById(1L)).thenReturn(Optional.of(treino));
        when(exercicioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemTreinoService.atualizar(itemId, dto));
    }

    @Test
    void deveExcluirItemTreinoComSucesso() throws RecursoNaoEncontradoException {
        Long id = 10L;
        ItemTreino item = criarItemTreinoMock(id, "4", "8", "50kg", 90, 1L, 2L);
        when(itemTreinoRepository.findById(id)).thenReturn(Optional.of(item));

        itemTreinoService.excluir(id);

        verify(itemTreinoRepository, times(1)).delete(item);
    }

    @Test
    void deveLancarExcecaoAoExcluirItemInexistente() {
        Long id = 99L;
        when(itemTreinoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemTreinoService.excluir(id));
        verify(itemTreinoRepository, times(0)).delete(any());
    }


    private Treino criarTreinoMock(Long id, String nome) {
        Treino treino = new Treino();
        treino.setId(id);
        treino.setNome(nome);
        return treino;
    }

    private Exercicio criarExercicioMock(Long id, String nome) {
        Exercicio exercicio = new Exercicio();
        exercicio.setId(id);
        exercicio.setNome(nome);
        return exercicio;
    }

    private ItemTreino criarItemTreinoMock(Long id, String series, String repeticoes, String carga, Integer descanso, Long treinoId, Long exercicioId) {
        Treino treino = criarTreinoMock(treinoId, "Treino Teste " + treinoId);
        Exercicio exercicio = criarExercicioMock(exercicioId, "Exercicio Teste " + exercicioId);
        return new ItemTreino(id, series, repeticoes, carga, descanso, treino, exercicio);
    }
}