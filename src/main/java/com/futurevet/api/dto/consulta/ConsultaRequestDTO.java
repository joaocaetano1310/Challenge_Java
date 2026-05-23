package com.futurevet.api.dto.consulta;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRequestDTO {

    @NotBlank(message = "Tipo de consulta é obrigatório")
    @Size(max = 100, message = "Tipo deve ter no máximo 100 caracteres")
    private String tipoConsulta;

    @NotNull(message = "Data é obrigatória")
    @FutureOrPresent(message = "Data não pode ser no passado")
    private LocalDate data;

    @NotNull(message = "Hora é obrigatória")
    private LocalTime hora;

    @NotBlank(message = "Local é obrigatório")
    @Size(max = 150, message = "Local deve ter no máximo 150 caracteres")
    private String local;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}
