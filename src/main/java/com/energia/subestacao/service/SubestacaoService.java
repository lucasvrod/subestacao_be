package com.energia.subestacao.service;

import com.energia.subestacao.dto.RedeMTDTO;
import com.energia.subestacao.dto.SubestacaoDTO;
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
public class SubestacaoService {

    private final SubestacaoRepository subestacaoRepository;
    private final RedeMTRepository redeMTRepository;

    @Autowired
    public SubestacaoService(SubestacaoRepository subestacaoRepository, RedeMTRepository redeMTRepository) {
        this.subestacaoRepository = subestacaoRepository;
        this.redeMTRepository = redeMTRepository;
    }

    @Transactional(readOnly = true)
    public List<SubestacaoDTO> findAll() {
        return subestacaoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubestacaoDTO findById(Integer id) {
        Subestacao subestacao = subestacaoRepository.findByIdWithRedesMT(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subestação", "id", id));
        return convertToDTO(subestacao);
    }

    @Transactional
    public SubestacaoDTO save(SubestacaoDTO subestacaoDTO) {
        validateUniqueCodeOnCreate(subestacaoDTO);
        Subestacao subestacao = convertToEntity(subestacaoDTO);

        List<RedeMT> redesMT = subestacao.getRedesMT();
        subestacao.setRedesMT(null);
        Subestacao savedSubestacao = subestacaoRepository.save(subestacao);

        if (redesMT != null && !redesMT.isEmpty()) {
            for (RedeMT redeMT : redesMT) {
                validateUniqueCodeOnCreate(redeMT);
                redeMT.setSubestacao(savedSubestacao);
            }
            savedSubestacao.setRedesMT(redesMT);
            savedSubestacao = subestacaoRepository.save(savedSubestacao);
        }

        return convertToDTO(savedSubestacao);
    }

    @Transactional
    public SubestacaoDTO update(Integer id, SubestacaoDTO subestacaoDTO) {
        Subestacao existingSubestacao = subestacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subestação", "id", id));

        validateUniqueCodeOnUpdate(subestacaoDTO, existingSubestacao);

        existingSubestacao.setNome(subestacaoDTO.getNome());
        existingSubestacao.setLatitude(subestacaoDTO.getLatitude());
        existingSubestacao.setLongitude(subestacaoDTO.getLongitude());

        updateRedesMT(existingSubestacao, subestacaoDTO.getRedesMT());

        Subestacao updatedSubestacao = subestacaoRepository.save(existingSubestacao);
        return convertToDTO(updatedSubestacao);
    }

    @Transactional
    public void delete(Integer id) {
        Subestacao subestacao = subestacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subestação", "id", id));

        subestacaoRepository.delete(subestacao);
    }

    private void updateRedesMT(Subestacao subestacao, List<RedeMTDTO> redeMTDTOs) {
        subestacao.getRedesMT().removeIf(redeMT ->
                redeMTDTOs.stream().noneMatch(dto ->
                        dto.getId() != null && dto.getId().equals(redeMT.getId())
                )
        );

        redeMTDTOs.forEach(redeMTDTO -> {
            if (redeMTDTO.getId() != null) {
                subestacao.getRedesMT().stream()
                        .filter(redeMT -> redeMT.getId().equals(redeMTDTO.getId()))
                        .findFirst()
                        .ifPresent(redeMT -> {
                            redeMT.setNome(redeMTDTO.getNome());
                            redeMT.setTensaoNominal(redeMTDTO.getTensaoNominal());
                        });
            } else {
                RedeMT newRedeMT = new RedeMT();
                newRedeMT.setCodigo(redeMTDTO.getCodigo());
                newRedeMT.setNome(redeMTDTO.getNome());
                newRedeMT.setTensaoNominal(redeMTDTO.getTensaoNominal());
                newRedeMT.setSubestacao(subestacao);

                validateUniqueCodeOnCreate(newRedeMT);
                subestacao.addRedeMT(newRedeMT);
            }
        });
    }

    private void validateUniqueCodeOnCreate(SubestacaoDTO subestacaoDTO) {
        if (subestacaoRepository.existsByCodigo(subestacaoDTO.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe uma subestação com o código: " + subestacaoDTO.getCodigo());
        }

        if (subestacaoDTO.getRedesMT() != null) {
            for (RedeMTDTO redeMTDTO : subestacaoDTO.getRedesMT()) {
                if (redeMTRepository.existsByCodigo(redeMTDTO.getCodigo())) {
                    throw new DataIntegrityViolationException("Já existe uma rede MT com o código: " + redeMTDTO.getCodigo());
                }
            }
        }
    }

    private void validateUniqueCodeOnCreate(RedeMT redeMT) {
        if (redeMTRepository.existsByCodigo(redeMT.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe uma rede MT com o código: " + redeMT.getCodigo());
        }
    }

    private void validateUniqueCodeOnUpdate(SubestacaoDTO subestacaoDTO, Subestacao existingSubestacao) {
        if (!existingSubestacao.getCodigo().equals(subestacaoDTO.getCodigo()) &&
                subestacaoRepository.existsByCodigo(subestacaoDTO.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe uma subestação com o código: " + subestacaoDTO.getCodigo());
        }
    }

    private SubestacaoDTO convertToDTO(Subestacao subestacao) {
        SubestacaoDTO dto = new SubestacaoDTO();
        dto.setId(subestacao.getId());
        dto.setCodigo(subestacao.getCodigo());
        dto.setNome(subestacao.getNome());
        dto.setLatitude(subestacao.getLatitude());
        dto.setLongitude(subestacao.getLongitude());

        if (subestacao.getRedesMT() != null) {
            dto.setRedesMT(subestacao.getRedesMT().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private RedeMTDTO convertToDTO(RedeMT redeMT) {
        RedeMTDTO dto = new RedeMTDTO();
        dto.setId(redeMT.getId());
        dto.setCodigo(redeMT.getCodigo());
        dto.setNome(redeMT.getNome());
        dto.setTensaoNominal(redeMT.getTensaoNominal());
        return dto;
    }

    private Subestacao convertToEntity(SubestacaoDTO dto) {
        Subestacao subestacao = new Subestacao();
        subestacao.setId(dto.getId());
        subestacao.setCodigo(dto.getCodigo());
        subestacao.setNome(dto.getNome());
        subestacao.setLatitude(dto.getLatitude());
        subestacao.setLongitude(dto.getLongitude());

        if (dto.getRedesMT() != null) {
            List<RedeMT> redesMT = dto.getRedesMT().stream()
                    .map(redeMTDTO -> {
                        RedeMT redeMT = new RedeMT();
                        redeMT.setId(redeMTDTO.getId());
                        redeMT.setCodigo(redeMTDTO.getCodigo());
                        redeMT.setNome(redeMTDTO.getNome());
                        redeMT.setTensaoNominal(redeMTDTO.getTensaoNominal());
                        redeMT.setSubestacao(subestacao);
                        return redeMT;
                    })
                    .collect(Collectors.toList());
            subestacao.setRedesMT(redesMT);
        }

        return subestacao;
    }
}