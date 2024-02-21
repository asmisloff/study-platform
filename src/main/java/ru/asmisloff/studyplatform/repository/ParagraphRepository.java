package ru.asmisloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.asmisloff.studyplatform.entity.Paragraph;

public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
}