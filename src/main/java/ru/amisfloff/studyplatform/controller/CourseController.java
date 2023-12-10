package ru.amisfloff.studyplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.*;
import ru.amisfloff.studyplatform.dto.CourseRequestToCreate;
import ru.amisfloff.studyplatform.dto.CourseRequestToUpdate;
import ru.amisfloff.studyplatform.entity.Course;
import ru.amisfloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.amisfloff.studyplatform.repository.CourseRepository;
import ru.amisfloff.studyplatform.validation.Verification;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;


@RestController
@RequestMapping("/course")
public class CourseController {

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable("id") Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    @GetMapping
    public List<Course> courseTable() {
        return courseRepository.findAll();
    }

    @GetMapping("/filter")
    public List<Course> findByTitleWithPrefix(
        @RequestParam(name = "titlePrefix", required = false) String titlePrefix
    ) {
        return courseRepository.findAll().stream()
            .filter(course -> course.getTitle().startsWith(requireNonNullElse(titlePrefix, "")))
            .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public void updateCourse(
        @PathVariable Long id,
        @Valid @RequestBody CourseRequestToUpdate request
    ) {
        Course course = courseRepository
            .findById(request.id())
            .orElseThrow(() -> new ResourceNotFoundException(id));
        course.setTitle(request.title());
        course.setAuthor(request.author());
        courseRepository.save(course);
    }

    @PostMapping
    public long createCourse(@Valid @RequestBody CourseRequestToCreate request) {
        Course course = new Course(null, request.title(), request.author());
        Long id = courseRepository.save(course).getId();
        logger.info("Course created, id = %d".formatted(id));
        return id;
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public void deleteCourse(@RequestParam long id) {
        if (!courseRepository.isExists(id)) {
            throw new ResourceNotFoundException(id);
        }
        courseRepository.deleteById(id);
    }

    private final CourseRepository courseRepository;
    private final Logger logger = Logger.getLogger(CourseController.class);
}
