package com.energia.subestacao.repository;

import com.energia.subestacao.model.RedeMT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedeMTRepository extends JpaRepository<RedeMT, Integer> {

    List<RedeMT> findBySubestacaoId(Integer subestacaoId);

    Optional<RedeMT> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}