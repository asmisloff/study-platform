package ru.asmisloff.studyplatform;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.asmisloff.studyplatform.dto.CourseRequestToCreate;
import ru.asmisloff.studyplatform.dto.CourseRequestToUpdate;
import ru.asmisloff.studyplatform.entity.Course;
import ru.asmisloff.studyplatform.repository.CourseRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


@Service
@AllArgsConstructor
public class CourseService {

    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public List<Course> findAllByTitleWithPrefix(@NotNull String prefix) {
        requireNonNull(prefix);
        return courseRepository.findAll().stream()
            .filter(course -> course.getTitle().startsWith(prefix))
            .collect(Collectors.toList());
    }

    public Course save(CourseRequestToCreate request) {
        Course course = new Course(null, request.title(), request.author());
        return courseRepository.save(course);
    }

    public void update(CourseRequestToUpdate request) {
        Course course = getById(request.id());
        course.setTitle(request.title());
//        course.setAuthor(request.author());
        courseRepository.save(course);
    }

    public void delete(long id) {
        if (!courseRepository.isExists(id)) throw new NoSuchElementException();
        courseRepository.deleteById(id);
    }

    private final CourseRepository courseRepository;
}