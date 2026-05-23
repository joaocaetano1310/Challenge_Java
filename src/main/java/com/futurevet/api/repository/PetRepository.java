package com.futurevet.api.repository;

import com.futurevet.api.entity.Pet;
import com.futurevet.api.entity.enums.EspeciePet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Page<Pet> findByUsuarioId(Long usuarioId, Pageable pageable);

    @Query("SELECT p FROM Pet p WHERE p.especie = :especie")
    Page<Pet> buscarPorEspecie(@Param("especie") EspeciePet especie, Pageable pageable);

    @Query("SELECT p FROM Pet p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Pet> buscarPorNome(@Param("nome") String nome, Pageable pageable);

    @Query("SELECT p FROM Pet p WHERE p.usuario.id = :usuarioId AND p.especie = :especie")
    Page<Pet> buscarPorUsuarioEEspecie(@Param("usuarioId") Long usuarioId,
                                       @Param("especie") EspeciePet especie,
                                       Pageable pageable);
}
