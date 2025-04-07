package com.energia.subestacao.service;

import com.energia.subestacao.dto.RedeMTDTO;
import com.energia.subestacao.exception.ResourceNotFoundException;
import com.energia.subestacao.model.RedeMT;
import com.energia.subestacao.model.Subestacao;
import com.energia.subestacao.repository.RedeMTRepository;
import com.energia.subestacao.repository.SubestacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedeMTServiceTest {

    @InjectMocks
    private RedeMTService redeMTService;

    @Mock
    private RedeMTRepository redeMTRepository;

    @Mock
    private SubestacaoRepository subestacaoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBySubestacaoId() {
        RedeMT rede = new RedeMT();
        rede.setId(1);
        rede.setCodigo("RMT001");
        rede.setNome("Rede 1");

        when(redeMTRepository.findBySubestacaoId(1)).thenReturn(List.of(rede));

        List<RedeMTDTO> result = redeMTService.findBySubestacaoId(1);

        assertEquals(1, result.size());
        assertEquals("Rede 1", result.get(0).getNome());
    }

    @Test
    void testFindById_Success() {
        RedeMT rede = new RedeMT();
        rede.setId(1);
        rede.setCodigo("RMT001");

        when(redeMTRepository.findById(1)).thenReturn(Optional.of(rede));

        RedeMTDTO result = redeMTService.findById(1);

        assertEquals("RMT001", result.getCodigo());
    }

    @Test
    void testFindById_NotFound() {
        when(redeMTRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> redeMTService.findById(99));
    }

    @Test
    void testSave_Sucesso() {
        RedeMTDTO dto = new RedeMTDTO();
        dto.setCodigo("RMT002");
        dto.setNome("Rede Nova");

        Subestacao sub = new Subestacao();
        sub.setId(1);

        RedeMT saved = new RedeMT();
        saved.setId(1);
        saved.setCodigo("RMT002");
        saved.setNome("Rede Nova");

        when(subestacaoRepository.findById(1)).thenReturn(Optional.of(sub));
        when(redeMTRepository.existsByCodigo("RMT002")).thenReturn(false);
        when(redeMTRepository.save(any(RedeMT.class))).thenReturn(saved);

        RedeMTDTO result = redeMTService.save(1, dto);

        assertEquals("RMT002", result.getCodigo());
    }

    @Test
    void testSave_SubestacaoNaoExiste() {
        RedeMTDTO dto = new RedeMTDTO();
        dto.setCodigo("RMT003");

        when(subestacaoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> redeMTService.save(999, dto));
    }

    @Test
    void testSave_CodigoDuplicado() {
        RedeMTDTO dto = new RedeMTDTO();
        dto.setCodigo("DUP001");

        Subestacao sub = new Subestacao();

        when(subestacaoRepository.findById(1)).thenReturn(Optional.of(sub));
        when(redeMTRepository.existsByCodigo("DUP001")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> redeMTService.save(1, dto));
    }

    @Test
    void testUpdate_Sucesso() {
        RedeMT existing = new RedeMT();
        existing.setId(1);
        existing.setCodigo("RMT001");

        RedeMTDTO dto = new RedeMTDTO();
        dto.setCodigo("RMT001");
        dto.setNome("Atualizado");

        when(redeMTRepository.findById(1)).thenReturn(Optional.of(existing));
        when(redeMTRepository.save(any(RedeMT.class))).thenReturn(existing);

        RedeMTDTO result = redeMTService.update(1, dto);

        assertEquals("Atualizado", result.getNome());
    }

    @Test
    void testUpdate_CodigoDuplicado() {
        RedeMT existing = new RedeMT();
        existing.setId(1);
        existing.setCodigo("OLD");

        RedeMTDTO dto = new RedeMTDTO();
        dto.setCodigo("NEW");

        when(redeMTRepository.findById(1)).thenReturn(Optional.of(existing));
        when(redeMTRepository.existsByCodigo("NEW")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> redeMTService.update(1, dto));
    }

    @Test
    void testDelete_Sucesso() {
        RedeMT rede = new RedeMT();
        rede.setId(1);

        when(redeMTRepository.findById(1)).thenReturn(Optional.of(rede));

        redeMTService.delete(1);

        verify(redeMTRepository).delete(rede);
    }

    @Test
    void testDelete_NotFound() {
        when(redeMTRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> redeMTService.delete(999));
    }
}
