package com.futurevet.api.controller;

import com.futurevet.api.dto.pet.PetRequestDTO;
import com.futurevet.api.dto.pet.PetResponseDTO;
import com.futurevet.api.entity.enums.EspeciePet;
import com.futurevet.api.service.PetService;
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
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Gerenciamento de pets dos usuários")
public class PetController {

    private final PetService petService;

    @PostMapping
    @Operation(summary = "Cadastrar novo pet")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pet criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<PetResponseDTO> cadastrar(@Valid @RequestBody PetRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.cadastrar(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pet por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pet encontrado"),
        @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<PetResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(petService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os pets com paginação e ordenação")
    public ResponseEntity<Page<PetResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(petService.listarTodos(pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar pets de um usuário específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Page<PetResponseDTO>> listarPorUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(petService.listarPorUsuario(usuarioId, pageable));
    }

    @GetMapping("/especie/{especie}")
    @Operation(summary = "Buscar pets por espécie (CAO, GATO, COELHO, OUTRO)")
    public ResponseEntity<Page<PetResponseDTO>> buscarPorEspecie(
            @PathVariable EspeciePet especie,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(petService.buscarPorEspecie(especie, pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pets por nome (parcial, case-insensitive)")
    public ResponseEntity<Page<PetResponseDTO>> buscarPorNome(
            @RequestParam String nome,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(petService.buscarPorNome(nome, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do pet")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pet atualizado"),
        @ApiResponse(responseCode = "404", description = "Pet ou Usuário não encontrado")
    })
    public ResponseEntity<PetResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PetRequestDTO dto) {
        return ResponseEntity.ok(petService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pet e todos seus registros vinculados")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pet deletado"),
        @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        petService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
