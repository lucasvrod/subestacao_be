package com.energia.subestacao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_SUBESTACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subestacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUBESTACAO")
    private Integer id;

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 3, message = "O código deve ter no máximo 3 caracteres")
    @Column(name = "CODIGO", length = 3, nullable = false, unique = true)
    private String codigo;

    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", length = 100)
    private String nome;

    @NotNull(message = "A latitude é obrigatória")
    @Column(name = "LATITUDE", precision = 15, scale = 13, nullable = false)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 15, scale = 13)
    private BigDecimal longitude;

    @OneToMany(mappedBy = "subestacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RedeMT> redesMT = new ArrayList<>();

    public void addRedeMT(RedeMT redeMT) {
        redesMT.add(redeMT);
        redeMT.setSubestacao(this);
    }

    public void removeRedeMT(RedeMT redeMT) {
        redesMT.remove(redeMT);
        redeMT.setSubestacao(null);
    }
}