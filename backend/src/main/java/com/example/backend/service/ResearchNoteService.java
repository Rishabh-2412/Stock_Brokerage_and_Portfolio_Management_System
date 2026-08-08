package com.example.backend.service;

import com.example.backend.dto.ResearchNoteDTO;

import java.util.List;

public interface ResearchNoteService {

    ResearchNoteDTO createNote(String username, ResearchNoteDTO request);

    List<ResearchNoteDTO> getAllNotes();

    List<ResearchNoteDTO> getNotesForSecurity(Long securityId);
}