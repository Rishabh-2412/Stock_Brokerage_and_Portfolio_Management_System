package com.example.backend.mapper;

import com.example.backend.dto.ResearchNoteDTO;
import com.example.backend.entity.ResearchNote;

public class ResearchNoteMapper {

    private ResearchNoteMapper() {
    }

    public static ResearchNoteDTO toDTO(ResearchNote note) {
        if (note == null) {
            return null;
        }
        return ResearchNoteDTO.builder()
                .noteId(note.getId())
                .authorId(note.getAuthor() != null ? note.getAuthor().getId() : null)
                .authorName(note.getAuthor() != null ? note.getAuthor().getUsername() : null)
                .securityId(note.getSecurity() != null ? note.getSecurity().getId() : null)
                .symbol(note.getSecurity() != null ? note.getSecurity().getSymbol() : null)
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .build();
    }
}