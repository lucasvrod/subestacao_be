package com.energia.subestacao.service;

import com.energia.subestacao.dto.RedeMTDTO;
import com.energia.subestacao.dto.SubestacaoDTO;
import com.energia.subestacao.exception.ResourceNotFoundException;
import com.energia.subestacao.model.RedeMT;
import com.energia.subestacao.model.Subestacao;
import com.energia.subestacao.repository.RedeMTRepository;
import com.energia.subestacao.repository.SubestacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubestacaoServiceTest {

    @InjectMocks
    private SubestacaoService subestacaoService;

    @Mock
    private SubestacaoRepository subestacaoRepository;

    @Mock
    private RedeMTRepository redeMTRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Subestacao sub = new Subestacao();
        sub.setId(1);
        sub.setCodigo("SUB001");
        sub.setNome("Subestação A");

        when(subestacaoRepository.findAll()).thenReturn(List.of(sub));

        List<SubestacaoDTO> result = subestacaoService.findAll();

        assertEquals(1, result.size());
        assertEquals("Subestação A", result.get(0).getNome());
    }

    @Test
    void testFindById_Success() {
        Subestacao sub = new Subestacao();
        sub.setId(1);
        sub.setCodigo("SUB001");
        sub.setNome("Subestação A");

        when(subestacaoRepository.findByIdWithRedesMT(1)).thenReturn(Optional.of(sub));

        SubestacaoDTO result = subestacaoService.findById(1);

        assertEquals("SUB001", result.getCodigo());
    }

    @Test
    void testFindById_NotFound() {
        when(subestacaoRepository.findByIdWithRedesMT(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subestacaoService.findById(99));
    }

    @Test
    void testSave_SubestacaoComCodigoDuplicado() {
        SubestacaoDTO dto = new SubestacaoDTO();
        dto.setCodigo("DUP001");

        when(subestacaoRepository.existsByCodigo("DUP001")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> subestacaoService.save(dto));
    }

    @Test
    void testSave_ComSucesso() {
        SubestacaoDTO dto = new SubestacaoDTO();
        dto.setCodigo("SUB002");
        dto.setNome("Nova Subestação");

        Subestacao saved = new Subestacao();
        saved.setId(1);
        saved.setCodigo("SUB002");
        saved.setNome("Nova Subestação");

        when(subestacaoRepository.existsByCodigo("SUB002")).thenReturn(false);
        when(subestacaoRepository.save(any(Subestacao.class))).thenReturn(saved);

        SubestacaoDTO result = subestacaoService.save(dto);

        assertNotNull(result);
        assertEquals("SUB002", result.getCodigo());
    }

    @Test
    void testUpdate_ComSucesso() {
        Integer id = 1;

        Subestacao existing = new Subestacao();
        existing.setId(id);
        existing.setCodigo("SUB001");
        existing.setNome("Antiga");

        SubestacaoDTO updatedDTO = new SubestacaoDTO();
        updatedDTO.setCodigo("SUB001");
        updatedDTO.setNome("Atualizada");

        when(subestacaoRepository.findById(id)).thenReturn(Optional.of(existing));
        when(subestacaoRepository.save(any(Subestacao.class))).thenReturn(existing);

        SubestacaoDTO result = subestacaoService.update(id, updatedDTO);

        assertEquals("Atualizada", result.getNome());
    }

    @Test
    void testDelete_ComSucesso() {
        Subestacao existing = new Subestacao();
        existing.setId(1);

        when(subestacaoRepository.findById(1)).thenReturn(Optional.of(existing));

        subestacaoService.delete(1);

        verify(subestacaoRepository).delete(existing);
    }

    @Test
    void testDelete_NotFound() {
        when(subestacaoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subestacaoService.delete(999));
    }
}
