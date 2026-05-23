package com.futurevet.api.dto.pet;

import com.futurevet.api.entity.enums.EspeciePet;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseDTO {
    private Long id;
    private String nome;
    private EspeciePet especie;
    private String especieDescricao;
    private String raca;
    private Integer idade;
    private String tamanho;
    private Double peso;
    private Long usuarioId;
    private String usuarioNome;
}
