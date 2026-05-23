package com.futurevet.api.service;

import com.futurevet.api.dto.vacina.VacinaRequestDTO;
import com.futurevet.api.dto.vacina.VacinaResponseDTO;
import com.futurevet.api.entity.Pet;
import com.futurevet.api.entity.Vacina;
import com.futurevet.api.exception.ResourceNotFoundException;
import com.futurevet.api.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository vacinaRepository;
    private final PetService petService;

    @Transactional
    @CacheEvict(value = "vacinas", allEntries = true)
    public VacinaResponseDTO cadastrar(VacinaRequestDTO dto) {
        Pet pet = petService.buscarEntidade(dto.getPetId());

        Vacina vacina = Vacina.builder()
                .nomeVacina(dto.getNomeVacina())
                .dataAplicacao(dto.getDataAplicacao())
                .proximaDose(dto.getProximaDose())
                .localAplicacao(dto.getLocalAplicacao())
                .pet(pet)
                .build();

        return toDTO(vacinaRepository.save(vacina));
    }

    @Cacheable(value = "vacinas", key = "#id")
    public VacinaResponseDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Cacheable(value = "vacinas-lista", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<VacinaResponseDTO> listarTodos(Pageable pageable) {
        return vacinaRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<VacinaResponseDTO> listarPorPet(Long petId, Pageable pageable) {
        petService.buscarEntidade(petId);
        return vacinaRepository.findByPetId(petId, pageable).map(this::toDTO);
    }

    public Page<VacinaResponseDTO> listarPorUsuario(Long usuarioId, Pageable pageable) {
        return vacinaRepository.buscarPorUsuario(usuarioId, pageable).map(this::toDTO);
    }

    public List<VacinaResponseDTO> buscarProximasDoses(int dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(dias);
        return vacinaRepository.buscarProximasDoses(hoje, limite)
                .stream().map(this::toDTO).toList();
    }

    public Page<VacinaResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return vacinaRepository.buscarPorNomeVacina(nome, pageable).map(this::toDTO);
    }

    @Transactional
    @CacheEvict(value = {"vacinas", "vacinas-lista"}, allEntries = true)
    public VacinaResponseDTO atualizar(Long id, VacinaRequestDTO dto) {
        Vacina vacina = buscarEntidade(id);
        Pet pet = petService.buscarEntidade(dto.getPetId());

        vacina.setNomeVacina(dto.getNomeVacina());
        vacina.setDataAplicacao(dto.getDataAplicacao());
        vacina.setProximaDose(dto.getProximaDose());
        vacina.setLocalAplicacao(dto.getLocalAplicacao());
        vacina.setPet(pet);

        return toDTO(vacinaRepository.save(vacina));
    }

    @Transactional
    @CacheEvict(value = {"vacinas", "vacinas-lista"}, allEntries = true)
    public void deletar(Long id) {
        buscarEntidade(id);
        vacinaRepository.deleteById(id);
    }

    private Vacina buscarEntidade(Long id) {
        return vacinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacina", id));
    }

    private VacinaResponseDTO toDTO(Vacina v) {
        return VacinaResponseDTO.builder()
                .id(v.getId())
                .nomeVacina(v.getNomeVacina())
                .dataAplicacao(v.getDataAplicacao())
                .proximaDose(v.getProximaDose())
                .localAplicacao(v.getLocalAplicacao())
                .petId(v.getPet().getId())
                .petNome(v.getPet().getNome())
                .build();
    }
}
