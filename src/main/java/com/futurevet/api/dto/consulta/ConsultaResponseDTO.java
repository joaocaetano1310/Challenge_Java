package com.futurevet.api.dto.consulta;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaResponseDTO {
    private Long id;
    private String tipoConsulta;
    private LocalDate data;
    private LocalTime hora;
    private String local;
    private Long petId;
    private String petNome;
}
