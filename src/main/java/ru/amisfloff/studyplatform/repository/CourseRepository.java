package ru.amisfloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.amisfloff.studyplatform.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
