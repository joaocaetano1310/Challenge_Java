package com.futurevet.api.dto.pet;

import com.futurevet.api.entity.enums.EspeciePet;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetRequestDTO {

    @NotBlank(message = "Nome do pet é obrigatório")
    @Size(max = 80, message = "Nome deve ter no máximo 80 caracteres")
    private String nome;

    @NotNull(message = "Espécie é obrigatória")
    private EspeciePet especie;

    private String raca; // opcional

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 0, message = "Idade deve ser maior ou igual a 0")
    @Max(value = 40, message = "Idade inválida")
    private Integer idade;

    @NotBlank(message = "Tamanho é obrigatório")
    private String tamanho;

    @NotNull(message = "Peso é obrigatório")
    @DecimalMin(value = "0.1", message = "Peso deve ser maior que 0")
    private Double peso;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
}
