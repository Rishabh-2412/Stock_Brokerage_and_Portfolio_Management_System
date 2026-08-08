package com.example.backend.service.impl;

import com.example.backend.dto.ResearchNoteDTO;
import com.example.backend.entity.ResearchNote;
import com.example.backend.entity.Security;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.ResearchNoteMapper;
import com.example.backend.repository.ResearchNoteRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ResearchNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResearchNoteServiceImpl implements ResearchNoteService {

    private final ResearchNoteRepository researchNoteRepository;
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;

    @Override
    public ResearchNoteDTO createNote(String username, ResearchNoteDTO request) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Security security = null;
        if (request.getSecurityId() != null) {
            security = securityRepository.findById(request.getSecurityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Security not found: " + request.getSecurityId()));
        }

        ResearchNote note = ResearchNote.builder()
                .author(author)
                .security(security)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return ResearchNoteMapper.toDTO(researchNoteRepository.save(note));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchNoteDTO> getAllNotes() {
        return researchNoteRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ResearchNoteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchNoteDTO> getNotesForSecurity(Long securityId) {
        if (!securityRepository.existsById(securityId)) {
            throw new ResourceNotFoundException("Security not found: " + securityId);
        }
        return researchNoteRepository.findBySecurityIdOrderByCreatedAtDesc(securityId)
                .stream()
                .map(ResearchNoteMapper::toDTO)
                .collect(Collectors.toList());
    }
}