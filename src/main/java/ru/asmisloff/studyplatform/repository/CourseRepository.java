package ru.asmisloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.asmisloff.studyplatform.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT COUNT(*) > 0 FROM Course c WHERE c.id = :id")
    boolean isExists(long id);

    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(:prefix) || '%'")
    List<Course> findAllByTitlesPrefix(String prefix);
}
