package ru.asmisloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.asmisloff.studyplatform.entity.StudentToCourse;
import ru.asmisloff.studyplatform.entity.StudentToCourseId;

public interface StudentToCourseRepository extends JpaRepository<StudentToCourse, StudentToCourseId> {
}