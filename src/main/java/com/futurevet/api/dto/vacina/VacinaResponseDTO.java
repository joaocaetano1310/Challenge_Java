package com.futurevet.api.dto.vacina;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacinaResponseDTO {
    private Long id;
    private String nomeVacina;
    private LocalDate dataAplicacao;
    private LocalDate proximaDose;
    private String localAplicacao;
    private Long petId;
    private String petNome;
}
