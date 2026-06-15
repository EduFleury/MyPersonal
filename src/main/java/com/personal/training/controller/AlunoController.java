package com.personal.training.controller;

import com.personal.training.dto.Aluno.AlunoRequestDTO;
import com.personal.training.dto.Aluno.AlunoResponseDTO;
import com.personal.training.dto.Usuario.UsuarioRequestFindByEmailDTO;
import com.personal.training.exception.RegraNegocioException;
import com.personal.training.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponseDTO criar(
            @RequestBody @Valid AlunoRequestDTO dto
    ) throws RegraNegocioException {

        return alunoService.criar(dto);
    }

    @GetMapping
    public List<AlunoResponseDTO> listarTodos() {

        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    public AlunoResponseDTO buscarPorId(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return alunoService.buscarPorId(id);
    }

    @GetMapping("/usuario/{id}")
    public AlunoResponseDTO buscarPorIdUsuario(
            @PathVariable Long id
    ) throws RegraNegocioException {

        return alunoService.buscarPorIdUsuario(id);
    }

    @GetMapping("/email")
    public AlunoResponseDTO buscarPorEmailUsuario(
            @RequestBody @Valid UsuarioRequestFindByEmailDTO dto
    ) throws RegraNegocioException {

        return alunoService.buscarPorEmailUsuario(dto);
    }

    @PutMapping("/{id}")
    public AlunoResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AlunoRequestDTO dto
    ) throws RegraNegocioException {

        return alunoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long id
    ) throws RegraNegocioException {

        alunoService.deletar(id);
    }

    @GetMapping("/meus-alunos")
    public List<AlunoResponseDTO> meusAlunos(
            Authentication authentication
    ) {

        return alunoService.listarMeusAlunos(
                authentication.getName()
        );
    }
}