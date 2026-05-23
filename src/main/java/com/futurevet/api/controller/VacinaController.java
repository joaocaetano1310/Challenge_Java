package com.futurevet.api.controller;

import com.futurevet.api.dto.vacina.VacinaRequestDTO;
import com.futurevet.api.dto.vacina.VacinaResponseDTO;
import com.futurevet.api.service.VacinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacinas")
@RequiredArgsConstructor
@Tag(name = "Vacinas", description = "Gerenciamento do cartão de vacinas dos pets")
public class VacinaController {

    private final VacinaService vacinaService;

    @PostMapping
    @Operation(summary = "Registrar nova vacina para um pet")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vacina registrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<VacinaResponseDTO> cadastrar(@Valid @RequestBody VacinaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vacinaService.cadastrar(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vacina por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vacina encontrada"),
        @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    public ResponseEntity<VacinaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vacinaService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas as vacinas com paginação")
    public ResponseEntity<Page<VacinaResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "dataAplicacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(vacinaService.listarTodos(pageable));
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "Listar vacinas de um pet específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada"),
        @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<Page<VacinaResponseDTO>> listarPorPet(
            @PathVariable Long petId,
            @PageableDefault(size = 10, sort = "dataAplicacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(vacinaService.listarPorPet(petId, pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar vacinas de todos os pets de um usuário")
    public ResponseEntity<Page<VacinaResponseDTO>> listarPorUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(vacinaService.listarPorUsuario(usuarioId, pageable));
    }

    @GetMapping("/proximas")
    @Operation(summary = "Listar próximas doses (padrão: próximos 30 dias)")
    @ApiResponse(responseCode = "200", description = "Lista de vacinas com dose próxima")
    public ResponseEntity<List<VacinaResponseDTO>> proximas(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(vacinaService.buscarProximasDoses(dias));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar vacinas por nome (parcial, case-insensitive)")
    public ResponseEntity<Page<VacinaResponseDTO>> buscarPorNome(
            @RequestParam String nome,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(vacinaService.buscarPorNome(nome, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro de vacina")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vacina atualizada"),
        @ApiResponse(responseCode = "404", description = "Vacina ou Pet não encontrado")
    })
    public ResponseEntity<VacinaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VacinaRequestDTO dto) {
        return ResponseEntity.ok(vacinaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro de vacina")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vacina deletada"),
        @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vacinaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
