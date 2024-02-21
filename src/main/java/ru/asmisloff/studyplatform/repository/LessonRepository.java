package ru.asmisloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.asmisloff.studyplatform.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}