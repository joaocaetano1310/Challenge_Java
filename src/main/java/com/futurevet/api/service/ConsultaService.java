package com.futurevet.api.service;

import com.futurevet.api.dto.consulta.ConsultaRequestDTO;
import com.futurevet.api.dto.consulta.ConsultaResponseDTO;
import com.futurevet.api.entity.Consulta;
import com.futurevet.api.entity.Pet;
import com.futurevet.api.exception.ResourceNotFoundException;
import com.futurevet.api.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PetService petService;

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public ConsultaResponseDTO cadastrar(ConsultaRequestDTO dto) {
        Pet pet = petService.buscarEntidade(dto.getPetId());

        Consulta consulta = Consulta.builder()
                .tipoConsulta(dto.getTipoConsulta())
                .data(dto.getData())
                .hora(dto.getHora())
                .local(dto.getLocal())
                .pet(pet)
                .build();

        return toDTO(consultaRepository.save(consulta));
    }

    @Cacheable(value = "consultas", key = "#id")
    public ConsultaResponseDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Cacheable(value = "consultas-lista", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<ConsultaResponseDTO> listarTodos(Pageable pageable) {
        return consultaRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<ConsultaResponseDTO> listarPorPet(Long petId, Pageable pageable) {
        petService.buscarEntidade(petId);
        return consultaRepository.findByPetIdOrderByDataAscHoraAsc(petId, pageable).map(this::toDTO);
    }

    public Page<ConsultaResponseDTO> listarPorUsuario(Long usuarioId, Pageable pageable) {
        return consultaRepository.buscarPorUsuario(usuarioId, pageable).map(this::toDTO);
    }

    public Page<ConsultaResponseDTO> buscarFuturas(Long usuarioId, Pageable pageable) {
        return consultaRepository.buscarConsultasFuturas(usuarioId, LocalDate.now(), pageable)
                .map(this::toDTO);
    }

    public Page<ConsultaResponseDTO> buscarPorTipo(String tipo, Pageable pageable) {
        return consultaRepository.buscarPorTipo(tipo, pageable).map(this::toDTO);
    }

    @Transactional
    @CacheEvict(value = {"consultas", "consultas-lista"}, allEntries = true)
    public ConsultaResponseDTO atualizar(Long id, ConsultaRequestDTO dto) {
        Consulta consulta = buscarEntidade(id);
        Pet pet = petService.buscarEntidade(dto.getPetId());

        consulta.setTipoConsulta(dto.getTipoConsulta());
        consulta.setData(dto.getData());
        consulta.setHora(dto.getHora());
        consulta.setLocal(dto.getLocal());
        consulta.setPet(pet);

        return toDTO(consultaRepository.save(consulta));
    }

    @Transactional
    @CacheEvict(value = {"consultas", "consultas-lista"}, allEntries = true)
    public void deletar(Long id) {
        buscarEntidade(id);
        consultaRepository.deleteById(id);
    }

    private Consulta buscarEntidade(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
    }

    private ConsultaResponseDTO toDTO(Consulta c) {
        return ConsultaResponseDTO.builder()
                .id(c.getId())
                .tipoConsulta(c.getTipoConsulta())
                .data(c.getData())
                .hora(c.getHora())
                .local(c.getLocal())
                .petId(c.getPet().getId())
                .petNome(c.getPet().getNome())
                .build();
    }
}
