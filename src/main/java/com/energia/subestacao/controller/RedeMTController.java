package com.energia.subestacao.controller;

import com.energia.subestacao.dto.RedeMTDTO;
import com.energia.subestacao.service.RedeMTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subestacoes/{subestacaoId}/redes-mt")
public class RedeMTController {

    private final RedeMTService redeMTService;

    @Autowired
    public RedeMTController(RedeMTService redeMTService) {
        this.redeMTService = redeMTService;
    }

    @GetMapping
    public ResponseEntity<List<RedeMTDTO>> getAllRedesMTBySubestacao(@PathVariable Integer subestacaoId) {
        List<RedeMTDTO> redesMT = redeMTService.findBySubestacaoId(subestacaoId);
        return ResponseEntity.ok(redesMT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RedeMTDTO> getRedeMTById(@PathVariable Integer id) {
        RedeMTDTO redeMT = redeMTService.findById(id);
        return ResponseEntity.ok(redeMT);
    }

    @PostMapping
    public ResponseEntity<RedeMTDTO> createRedeMT(
            @PathVariable Integer subestacaoId,
            @Valid @RequestBody RedeMTDTO redeMTDTO) {
        RedeMTDTO createdRedeMT = redeMTService.save(subestacaoId, redeMTDTO);
        return new ResponseEntity<>(createdRedeMT, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RedeMTDTO> updateRedeMT(
            @PathVariable Integer id,
            @Valid @RequestBody RedeMTDTO redeMTDTO) {
        RedeMTDTO updatedRedeMT = redeMTService.update(id, redeMTDTO);
        return ResponseEntity.ok(updatedRedeMT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRedeMT(@PathVariable Integer id) {
        redeMTService.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rede MT excluída com sucesso!");

        return ResponseEntity.ok(response);
    }
}