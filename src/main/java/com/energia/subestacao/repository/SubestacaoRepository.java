package com.energia.subestacao.repository;

import com.energia.subestacao.model.Subestacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubestacaoRepository extends JpaRepository<Subestacao, Integer> {

    Optional<Subestacao> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    @Query("SELECT s FROM Subestacao s LEFT JOIN FETCH s.redesMT WHERE s.id = :id")
    Optional<Subestacao> findByIdWithRedesMT(Integer id);
}