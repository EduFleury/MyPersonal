package com.personal.training.service;

import com.personal.training.dto.Exercicio.ExercicioRequestDTO;
import com.personal.training.dto.Exercicio.ExercicioResponseDTO;
import com.personal.training.dto.Usuario.UsuarioResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.model.Exercicio;
import com.personal.training.model.Usuario;
import com.personal.training.repository.ExercicioRepository;
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
class ExercicioServiceTest {

    @InjectMocks
    private ExercicioService exercicioService;

    @Mock
    private ExercicioRepository exercicioRepository;

    @Test
    void deveCriarExercicioComSucesso() {
        ExercicioRequestDTO dto = new ExercicioRequestDTO("Supino Reto", "PEITO", "Exercício com barra livre");

        Exercicio exercicioSalvo = new Exercicio();
        exercicioSalvo.setId(1L);
        exercicioSalvo.setNome("Supino Reto");
        exercicioSalvo.setGrupoMuscular("PEITO");
        exercicioSalvo.setDescricao("Exercício com barra livre");

        when(exercicioRepository.save(any(Exercicio.class))).thenReturn(exercicioSalvo);

        ExercicioResponseDTO response = exercicioService.criar(dto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Supino Reto", response.nome());
        assertEquals("Exercício com barra livre", response.descricao());
    }

    @Test
    void deveListarTodosOsExercicios() {
        Exercicio exercicio = criarExercicioMock(1L, "Agachamento", "PERNAS");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercicio> paginaDeExercicios = new PageImpl<>(List.of(exercicio));
        when(exercicioRepository.findAll(pageable)).thenReturn(paginaDeExercicios);

        Page<ExercicioResponseDTO> resultado = exercicioService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals("Agachamento", resultado.getContent().get(0).nome());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverExercicios() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercicio> paginaDeExercicios = new PageImpl<>(Collections.emptyList());

        when(exercicioRepository.findAll(pageable)).thenReturn(paginaDeExercicios);

        Page<ExercicioResponseDTO> resultado = exercicioService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveBuscarExercicioPorIdComSucesso() throws RecursoNaoEncontradoException {
        Long id = 1L;
        Exercicio exercicio = criarExercicioMock(id, "Rosca Direta", "BRAÇOS");
        when(exercicioRepository.findById(id)).thenReturn(Optional.of(exercicio));

        ExercicioResponseDTO response = exercicioService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("Rosca Direta", response.nome());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        Long id = 99L;
        when(exercicioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> exercicioService.buscarPorId(id));
    }

    @Test
    void deveAtualizarExercicioComSucesso() throws RecursoNaoEncontradoException {
        Long id = 1L;
        ExercicioRequestDTO dto = new ExercicioRequestDTO("Supino Inclinado", "PEITO", "Uso de halteres");

        Exercicio exercicioExistente = criarExercicioMock(id, "Supino Reto", "PEITO");

        Exercicio exercicioAtualizado = new Exercicio();
        exercicioAtualizado.setId(id);
        exercicioAtualizado.setNome(dto.nome());
        exercicioAtualizado.setGrupoMuscular(dto.grupoMuscular());
        exercicioAtualizado.setDescricao(dto.descricao());

        when(exercicioRepository.findById(id)).thenReturn(Optional.of(exercicioExistente));
        when(exercicioRepository.save(any(Exercicio.class))).thenReturn(exercicioAtualizado);

        ExercicioResponseDTO response = exercicioService.atualizar(id, dto);

        assertNotNull(response);
        assertEquals("Supino Inclinado", response.nome());
        assertEquals("Uso de halteres", response.descricao());
    }

    @Test
    void deveLancarExcecaoAoAtualizarExercicioInexistente() {
        Long id = 99L;
        ExercicioRequestDTO dto = new ExercicioRequestDTO("Nome", "Grupo", "Descricao");

        when(exercicioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> exercicioService.atualizar(id, dto));
        verify(exercicioRepository, times(0)).save(any());
    }

    @Test
    void deveExcluirExercicioComSucesso() throws RecursoNaoEncontradoException {
        Long id = 1L;
        Exercicio exercicio = criarExercicioMock(id, "Exercício deletado", "Costas");
        when(exercicioRepository.findById(id)).thenReturn(Optional.of(exercicio));

        exercicioService.excluir(id);

        verify(exercicioRepository, times(1)).delete(exercicio);
    }

    @Test
    void deveLancarExcecaoAoExcluirExercicioInexistente() {
        Long id = 99L;
        when(exercicioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> exercicioService.excluir(id));
        verify(exercicioRepository, times(0)).delete(any());
    }

    private Exercicio criarExercicioMock(Long id, String nome, String grupoMuscular) {
        Exercicio exercicio = new Exercicio();
        exercicio.setId(id);
        exercicio.setNome(nome);
        exercicio.setGrupoMuscular(grupoMuscular);
        exercicio.setDescricao("Descrição padrão do exercício de teste");
        return exercicio;
    }
}