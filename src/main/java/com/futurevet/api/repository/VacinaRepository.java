package com.futurevet.api.repository;

import com.futurevet.api.entity.Vacina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    Page<Vacina> findByPetId(Long petId, Pageable pageable);

    @Query("SELECT v FROM Vacina v WHERE v.pet.usuario.id = :usuarioId")
    Page<Vacina> buscarPorUsuario(@Param("usuarioId") Long usuarioId, Pageable pageable);

    @Query("SELECT v FROM Vacina v WHERE v.proximaDose BETWEEN :hoje AND :limite ORDER BY v.proximaDose ASC")
    List<Vacina> buscarProximasDoses(@Param("hoje") LocalDate hoje, @Param("limite") LocalDate limite);

    @Query("SELECT v FROM Vacina v WHERE LOWER(v.nomeVacina) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Vacina> buscarPorNomeVacina(@Param("nome") String nome, Pageable pageable);
}
