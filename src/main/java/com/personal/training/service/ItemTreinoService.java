package com.personal.training.service;

import com.personal.training.dto.ItemTreino.ItemTreinoRequestDTO;
import com.personal.training.dto.ItemTreino.ItemTreinoResponseDTO;
import com.personal.training.exception.RecursoNaoEncontradoException;
import com.personal.training.model.Exercicio;
import com.personal.training.model.ItemTreino;
import com.personal.training.model.Treino;
import com.personal.training.repository.ExercicioRepository;
import com.personal.training.repository.ItemTreinoRepository;
import com.personal.training.repository.TreinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemTreinoService {

    private final ItemTreinoRepository itemTreinoRepository;
    private final TreinoRepository treinoRepository;
    private final ExercicioRepository exercicioRepository;

    public ItemTreinoResponseDTO criar(ItemTreinoRequestDTO dto) throws RecursoNaoEncontradoException{

        Treino treino = treinoRepository.findById(dto.treinoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Treino não encontrado"));

        Exercicio exercicio = exercicioRepository.findById(dto.exercicioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado"));

        ItemTreino item = new ItemTreino();

        item.setSeries(dto.series());
        item.setRepeticoes(dto.repeticoes());
        item.setCarga(dto.carga());
        item.setDescansoSegundos(dto.descansoSegundos());
        item.setTreino(treino);
        item.setExercicio(exercicio);

        item = itemTreinoRepository.save(item);

        return toDTO(item);
    }

    public List<ItemTreinoResponseDTO> listar() {

        return itemTreinoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ItemTreinoResponseDTO buscarPorId(Long id) throws RecursoNaoEncontradoException{

        ItemTreino item = itemTreinoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item treino não encontrado"));

        return toDTO(item);
    }

    public ItemTreinoResponseDTO atualizar(
            Long id,
            ItemTreinoRequestDTO dto
    ) throws RecursoNaoEncontradoException{

        ItemTreino item = itemTreinoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item treino não encontrado"));

        Treino treino = treinoRepository.findById(dto.treinoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Treino não encontrado"));

        Exercicio exercicio = exercicioRepository.findById(dto.exercicioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado"));

        item.setSeries(dto.series());
        item.setRepeticoes(dto.repeticoes());
        item.setCarga(dto.carga());
        item.setDescansoSegundos(dto.descansoSegundos());
        item.setTreino(treino);
        item.setExercicio(exercicio);

        item = itemTreinoRepository.save(item);

        return toDTO(item);
    }

    public void excluir(Long id) throws RecursoNaoEncontradoException{

        ItemTreino item = itemTreinoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item treino não encontrado"));

        itemTreinoRepository.delete(item);
    }

    private ItemTreinoResponseDTO toDTO(ItemTreino item) {

        return new ItemTreinoResponseDTO(
                item.getId(),
                item.getSeries(),
                item.getRepeticoes(),
                item.getCarga(),
                item.getDescansoSegundos(),
                item.getTreino().getId(),
                item.getTreino().getNome(),
                item.getExercicio().getId(),
                item.getExercicio().getNome()
        );
    }
}