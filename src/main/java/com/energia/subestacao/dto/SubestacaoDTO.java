package com.energia.subestacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubestacaoDTO {

    private Integer id;

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 3, message = "O código deve ter no máximo 3 caracteres")
    private String codigo;

    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "A latitude é obrigatória")
    private BigDecimal latitude;

    private BigDecimal longitude;

    @Valid
    private List<RedeMTDTO> redesMT = new ArrayList<>();
}