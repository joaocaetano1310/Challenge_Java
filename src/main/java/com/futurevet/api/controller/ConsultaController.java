package com.futurevet.api.controller;

import com.futurevet.api.dto.consulta.ConsultaRequestDTO;
import com.futurevet.api.dto.consulta.ConsultaResponseDTO;
import com.futurevet.api.service.ConsultaService;
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

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Agendamento e gestão de consultas veterinárias")
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    @Operation(summary = "Agendar nova consulta veterinária")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Consulta agendada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<ConsultaResponseDTO> cadastrar(@Valid @RequestBody ConsultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.cadastrar(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas as consultas com paginação, ordenadas por data")
    public ResponseEntity<Page<ConsultaResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "data", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(consultaService.listarTodos(pageable));
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "Listar consultas de um pet, ordenadas por data e hora")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada"),
        @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<Page<ConsultaResponseDTO>> listarPorPet(
            @PathVariable Long petId,
            @PageableDefault(size = 10, sort = "data") Pageable pageable) {
        return ResponseEntity.ok(consultaService.listarPorPet(petId, pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar todas as consultas dos pets de um usuário")
    public ResponseEntity<Page<ConsultaResponseDTO>> listarPorUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(consultaService.listarPorUsuario(usuarioId, pageable));
    }

    @GetMapping("/usuario/{usuarioId}/futuras")
    @Operation(summary = "Listar consultas futuras dos pets de um usuário")
    public ResponseEntity<Page<ConsultaResponseDTO>> buscarFuturas(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(consultaService.buscarFuturas(usuarioId, pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar consultas por tipo (parcial, case-insensitive)")
    public ResponseEntity<Page<ConsultaResponseDTO>> buscarPorTipo(
            @RequestParam String tipo,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(consultaService.buscarPorTipo(tipo, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma consulta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta atualizada"),
        @ApiResponse(responseCode = "404", description = "Consulta ou Pet não encontrado")
    })
    public ResponseEntity<ConsultaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConsultaRequestDTO dto) {
        return ResponseEntity.ok(consultaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar/deletar uma consulta")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Consulta deletada"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        consultaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
