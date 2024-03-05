package ru.asmisloff.studyplatform.service;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asmisloff.studyplatform.dto.CourseRequestToCreate;
import ru.asmisloff.studyplatform.dto.CourseRequestToUpdate;
import ru.asmisloff.studyplatform.entity.Course;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.asmisloff.studyplatform.repository.CourseRepository;
import ru.asmisloff.studyplatform.repository.UserRepository;

import java.util.List;

import static ru.asmisloff.studyplatform.entity.Resource.COURSE;
import static ru.asmisloff.studyplatform.entity.Resource.USER;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COURSE, id));
    }

    @Transactional(readOnly = true)
    public Page<Course> getPage(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Course> findAllByTitleWithPrefix(@Nullable String prefix) {
        if (prefix == null) {
            return courseRepository.findAll();
        } else {
            return courseRepository.findAllByTitlesPrefix(prefix);
        }
    }

    @Transactional
    public Course save(CourseRequestToCreate request, long userId) {
        if (userRepository.isActive(userId)) {
            request.validate().throwIfNotEmpty();
            User user = userRepository.getById(userId);
            Course course = new Course(request.title(), "", user);
            return courseRepository.save(course);
        } else {
            throw new ResourceNotFoundException(USER, userId);
        }
    }

    @Transactional
    public void update(CourseRequestToUpdate request) {
        Course course = getById(request.id());
        course.setTitle(request.title());
        courseRepository.save(course);
    }

    @Transactional
    public void delete(long id) {
        courseRepository.deleteById(id);
    }
}