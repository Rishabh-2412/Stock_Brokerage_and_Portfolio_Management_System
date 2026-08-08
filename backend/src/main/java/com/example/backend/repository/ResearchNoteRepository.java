package com.example.backend.repository;

import com.example.backend.entity.ResearchNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResearchNoteRepository extends JpaRepository<ResearchNote, Long> {

    List<ResearchNote> findBySecurityIdOrderByCreatedAtDesc(Long securityId);

    List<ResearchNote> findAllByOrderByCreatedAtDesc();
}