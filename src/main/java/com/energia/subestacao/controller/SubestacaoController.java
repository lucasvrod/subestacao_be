package com.energia.subestacao.controller;

import com.energia.subestacao.dto.SubestacaoDTO;
import com.energia.subestacao.service.SubestacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subestacoes")
public class SubestacaoController {

    private final SubestacaoService subestacaoService;

    @Autowired
    public SubestacaoController(SubestacaoService subestacaoService) {
        this.subestacaoService = subestacaoService;
    }

    @GetMapping
    public ResponseEntity<List<SubestacaoDTO>> getAllSubestacoes() {
        List<SubestacaoDTO> subestacoes = subestacaoService.findAll();
        return ResponseEntity.ok(subestacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubestacaoDTO> getSubestacaoById(@PathVariable Integer id) {
        SubestacaoDTO subestacao = subestacaoService.findById(id);
        return ResponseEntity.ok(subestacao);
    }

    @PostMapping
    public ResponseEntity<SubestacaoDTO> createSubestacao(@Valid @RequestBody SubestacaoDTO subestacaoDTO) {
        SubestacaoDTO createdSubestacao = subestacaoService.save(subestacaoDTO);
        return new ResponseEntity<>(createdSubestacao, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubestacaoDTO> updateSubestacao(
            @PathVariable Integer id,
            @Valid @RequestBody SubestacaoDTO subestacaoDTO) {
        SubestacaoDTO updatedSubestacao = subestacaoService.update(id, subestacaoDTO);
        return ResponseEntity.ok(updatedSubestacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubestacao(@PathVariable Integer id) {
        subestacaoService.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Subestação excluída com sucesso!");

        return ResponseEntity.ok(response);
    }
}