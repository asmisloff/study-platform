package ru.amisfloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.amisfloff.studyplatform.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT COUNT(*) > 0 FROM Course c WHERE c.id = :id")
    boolean isExists(long id);
}
