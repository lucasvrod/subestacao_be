package com.energia.subestacao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_REDE_MT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedeMT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REDE_MT")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUBESTACAO", nullable = false)
    @JsonIgnore
    private Subestacao subestacao;

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 5, message = "O código deve ter no máximo 5 caracteres")
    @Column(name = "CODIGO", length = 5, nullable = false, unique = true)
    private String codigo;

    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", length = 100)
    private String nome;

    @Column(name = "TENSAO_NOMINAL", precision = 5, scale = 2)
    private BigDecimal tensaoNominal;

}