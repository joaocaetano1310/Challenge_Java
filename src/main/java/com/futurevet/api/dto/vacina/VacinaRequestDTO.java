package com.futurevet.api.dto.vacina;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacinaRequestDTO {

    @NotBlank(message = "Nome da vacina é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nomeVacina;

    @NotNull(message = "Data de aplicação é obrigatória")
    @PastOrPresent(message = "Data de aplicação não pode ser futura")
    private LocalDate dataAplicacao;

    @NotNull(message = "Data da próxima dose é obrigatória")
    @FutureOrPresent(message = "Próxima dose deve ser hoje ou uma data futura")
    private LocalDate proximaDose;

    @NotBlank(message = "Local de aplicação é obrigatório")
    @Size(max = 150, message = "Local deve ter no máximo 150 caracteres")
    private String localAplicacao;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}
