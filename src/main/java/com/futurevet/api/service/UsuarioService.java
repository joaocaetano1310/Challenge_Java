package com.futurevet.api.service;

import com.futurevet.api.dto.usuario.LoginRequestDTO;
import com.futurevet.api.dto.usuario.UsuarioRequestDTO;
import com.futurevet.api.dto.usuario.UsuarioResponseDTO;
import com.futurevet.api.entity.Usuario;
import com.futurevet.api.exception.BusinessException;
import com.futurevet.api.exception.ResourceNotFoundException;
import com.futurevet.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + dto.getEmail());
        }
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("CPF já cadastrado: " + dto.getCpf());
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .cpf(dto.getCpf())
                .telefone(dto.getTelefone())
                .build();

        return toDTO(usuarioRepository.save(usuario));
    }

    @Cacheable(value = "usuarios", key = "#id")
    public UsuarioResponseDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Cacheable(value = "usuarios-lista", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<UsuarioResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return usuarioRepository.buscarPorNome(nome, pageable).map(this::toDTO);
    }

    @Transactional
    @CacheEvict(value = {"usuarios", "usuarios-lista"}, allEntries = true)
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = buscarEntidade(id);

        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + dto.getEmail());
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setCpf(dto.getCpf());
        usuario.setTelefone(dto.getTelefone());

        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    @CacheEvict(value = {"usuarios", "usuarios-lista"}, allEntries = true)
    public void deletar(Long id) {
        buscarEntidade(id);
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.login(dto.getEmail(), dto.getSenha())
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));
        return toDTO(usuario);
    }

    public Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    private UsuarioResponseDTO toDTO(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .cpf(u.getCpf())
                .telefone(u.getTelefone())
                .build();
    }
}
