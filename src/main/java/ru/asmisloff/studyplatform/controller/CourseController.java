package ru.asmisloff.studyplatform.controller;

import lombok.AllArgsConstructor;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.asmisloff.studyplatform.dto.CourseRequestToCreate;
import ru.asmisloff.studyplatform.dto.CourseRequestToUpdate;
import ru.asmisloff.studyplatform.entity.Course;
import ru.asmisloff.studyplatform.service.CourseService;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.requireNonNullElse;


@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable("id") Long id) { return courseService.getById(id); }

    @GetMapping
    public List<Course> courseTable() { return courseService.getAll(); }

    @GetMapping("/filter")
    public List<Course> findByTitleWithPrefix(
        @Nullable @RequestParam(name = "titlePrefix", required = false) String titlePrefix
    ) {
        return courseService.findAllByTitleWithPrefix(requireNonNullElse(titlePrefix, ""));
    }

    @PutMapping
    public void updateCourse(@Valid @RequestBody CourseRequestToUpdate request) {
        courseService.update(request);
    }

    @PostMapping
    public long createCourse(@RequestBody CourseRequestToCreate request, @RequestParam("id") long id) {
        return courseService.save(request, id).getId();
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@RequestParam long id) {
        courseService.delete(id);
    }

    private final CourseService courseService;
    private final Logger logger = Logger.getLogger(CourseController.class);
}
