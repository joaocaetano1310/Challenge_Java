package com.futurevet.api.repository;

import com.futurevet.api.entity.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findByPetId(Long petId, Pageable pageable);

    Page<Consulta> findByPetIdOrderByDataAscHoraAsc(Long petId, Pageable pageable);

    @Query("SELECT c FROM Consulta c WHERE c.pet.usuario.id = :usuarioId ORDER BY c.data ASC, c.hora ASC")
    Page<Consulta> buscarPorUsuario(@Param("usuarioId") Long usuarioId, Pageable pageable);

    @Query("SELECT c FROM Consulta c WHERE c.pet.usuario.id = :usuarioId AND c.data >= :hoje ORDER BY c.data ASC")
    Page<Consulta> buscarConsultasFuturas(@Param("usuarioId") Long usuarioId,
                                          @Param("hoje") LocalDate hoje,
                                          Pageable pageable);

    @Query("SELECT c FROM Consulta c WHERE LOWER(c.tipoConsulta) LIKE LOWER(CONCAT('%', :tipo, '%'))")
    Page<Consulta> buscarPorTipo(@Param("tipo") String tipo, Pageable pageable);
}
