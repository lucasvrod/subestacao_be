package com.energia.subestacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedeMTDTO {

    private Integer id;

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 5, message = "O código deve ter no máximo 5 caracteres")
    private String codigo;

    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    private BigDecimal tensaoNominal;
}