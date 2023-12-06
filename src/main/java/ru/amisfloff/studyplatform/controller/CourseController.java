package ru.amisfloff.studyplatform.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.amisfloff.studyplatform.dto.CourseRequestToCreate;
import ru.amisfloff.studyplatform.dto.CourseRequestToUpdate;
import ru.amisfloff.studyplatform.entity.Course;
import ru.amisfloff.studyplatform.repository.CourseRepository;

import java.util.List;


@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseRepository courseRepository;

    @Autowired
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

    @PutMapping("/{id}")
    public void updateCourse(@PathVariable Long id,
                             @Valid @RequestBody CourseRequestToUpdate request) {
        Course course = courseRepository.findById(request.id()).orElseThrow();
        course.setTitle(request.title());
        course.setAuthor(request.author());
        courseRepository.save(course);
    }

    @PostMapping
    public Course createCourse(@Valid @RequestBody CourseRequestToCreate request) {
        Course course = Course.builder()
            .author(request.author())
            .title(request.title())
            .build();
        return courseRepository.save(course);
    }
}
