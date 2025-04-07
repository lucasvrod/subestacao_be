package com.energia.subestacao.service;

import com.energia.subestacao.dto.RedeMTDTO;
import com.energia.subestacao.exception.ResourceNotFoundException;
import com.energia.subestacao.model.RedeMT;
import com.energia.subestacao.model.Subestacao;
import com.energia.subestacao.repository.RedeMTRepository;
import com.energia.subestacao.repository.SubestacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedeMTService {

    private final RedeMTRepository redeMTRepository;
    private final SubestacaoRepository subestacaoRepository;

    @Autowired
    public RedeMTService(RedeMTRepository redeMTRepository, SubestacaoRepository subestacaoRepository) {
        this.redeMTRepository = redeMTRepository;
        this.subestacaoRepository = subestacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<RedeMTDTO> findBySubestacaoId(Integer subestacaoId) {
        return redeMTRepository.findBySubestacaoId(subestacaoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RedeMTDTO findById(Integer id) {
        RedeMT redeMT = redeMTRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rede MT", "id", id));
        return convertToDTO(redeMT);
    }

    @Transactional
    public RedeMTDTO save(Integer subestacaoId, RedeMTDTO redeMTDTO) {
        Subestacao subestacao = subestacaoRepository.findById(subestacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Subestação", "id", subestacaoId));

        if (redeMTRepository.existsByCodigo(redeMTDTO.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe uma rede MT com o código: " + redeMTDTO.getCodigo());
        }

        RedeMT redeMT = convertToEntity(redeMTDTO);
        redeMT.setSubestacao(subestacao);

        RedeMT savedRedeMT = redeMTRepository.save(redeMT);
        return convertToDTO(savedRedeMT);
    }

    @Transactional
    public RedeMTDTO update(Integer id, RedeMTDTO redeMTDTO) {
        RedeMT existingRedeMT = redeMTRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rede MT", "id", id));

        if (!existingRedeMT.getCodigo().equals(redeMTDTO.getCodigo()) &&
                redeMTRepository.existsByCodigo(redeMTDTO.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe uma rede MT com o código: " + redeMTDTO.getCodigo());
        }

        existingRedeMT.setNome(redeMTDTO.getNome());
        existingRedeMT.setTensaoNominal(redeMTDTO.getTensaoNominal());

        RedeMT updatedRedeMT = redeMTRepository.save(existingRedeMT);
        return convertToDTO(updatedRedeMT);
    }

    @Transactional
    public void delete(Integer id) {
        RedeMT redeMT = redeMTRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rede MT", "id", id));

        redeMTRepository.delete(redeMT);
    }

    private RedeMTDTO convertToDTO(RedeMT redeMT) {
        RedeMTDTO dto = new RedeMTDTO();
        dto.setId(redeMT.getId());
        dto.setCodigo(redeMT.getCodigo());
        dto.setNome(redeMT.getNome());
        dto.setTensaoNominal(redeMT.getTensaoNominal());
        return dto;
    }

    private RedeMT convertToEntity(RedeMTDTO dto) {
        RedeMT redeMT = new RedeMT();
        redeMT.setId(dto.getId());
        redeMT.setCodigo(dto.getCodigo());
        redeMT.setNome(dto.getNome());
        redeMT.setTensaoNominal(dto.getTensaoNominal());
        return redeMT;
    }
}