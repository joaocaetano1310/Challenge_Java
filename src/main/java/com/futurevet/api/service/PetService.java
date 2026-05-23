package com.futurevet.api.service;

import com.futurevet.api.dto.pet.PetRequestDTO;
import com.futurevet.api.dto.pet.PetResponseDTO;
import com.futurevet.api.entity.Pet;
import com.futurevet.api.entity.Usuario;
import com.futurevet.api.entity.enums.EspeciePet;
import com.futurevet.api.exception.ResourceNotFoundException;
import com.futurevet.api.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UsuarioService usuarioService;

    @Transactional
    @CacheEvict(value = "pets", allEntries = true)
    public PetResponseDTO cadastrar(PetRequestDTO dto) {
        Usuario usuario = usuarioService.buscarEntidade(dto.getUsuarioId());

        Pet pet = Pet.builder()
                .nome(dto.getNome())
                .especie(dto.getEspecie())
                .raca(dto.getRaca())
                .idade(dto.getIdade())
                .tamanho(dto.getTamanho())
                .peso(dto.getPeso())
                .usuario(usuario)
                .build();

        return toDTO(petRepository.save(pet));
    }

    @Cacheable(value = "pets", key = "#id")
    public PetResponseDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Cacheable(value = "pets-lista", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<PetResponseDTO> listarTodos(Pageable pageable) {
        return petRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<PetResponseDTO> listarPorUsuario(Long usuarioId, Pageable pageable) {
        usuarioService.buscarEntidade(usuarioId);
        return petRepository.findByUsuarioId(usuarioId, pageable).map(this::toDTO);
    }

    public Page<PetResponseDTO> buscarPorEspecie(EspeciePet especie, Pageable pageable) {
        return petRepository.buscarPorEspecie(especie, pageable).map(this::toDTO);
    }

    public Page<PetResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return petRepository.buscarPorNome(nome, pageable).map(this::toDTO);
    }

    @Transactional
    @CacheEvict(value = {"pets", "pets-lista"}, allEntries = true)
    public PetResponseDTO atualizar(Long id, PetRequestDTO dto) {
        Pet pet = buscarEntidade(id);
        Usuario usuario = usuarioService.buscarEntidade(dto.getUsuarioId());

        pet.setNome(dto.getNome());
        pet.setEspecie(dto.getEspecie());
        pet.setRaca(dto.getRaca());
        pet.setIdade(dto.getIdade());
        pet.setTamanho(dto.getTamanho());
        pet.setPeso(dto.getPeso());
        pet.setUsuario(usuario);

        return toDTO(petRepository.save(pet));
    }

    @Transactional
    @CacheEvict(value = {"pets", "pets-lista"}, allEntries = true)
    public void deletar(Long id) {
        buscarEntidade(id);
        petRepository.deleteById(id);
    }

    public Pet buscarEntidade(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet", id));
    }

    private PetResponseDTO toDTO(Pet p) {
        return PetResponseDTO.builder()
                .id(p.getId())
                .nome(p.getNome())
                .especie(p.getEspecie())
                .especieDescricao(p.getEspecie().getDescricao())
                .raca(p.getRaca())
                .idade(p.getIdade())
                .tamanho(p.getTamanho())
                .peso(p.getPeso())
                .usuarioId(p.getUsuario().getId())
                .usuarioNome(p.getUsuario().getNome())
                .build();
    }
}
