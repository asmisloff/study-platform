package ru.asmisloff.studyplatform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.asmisloff.studyplatform.dto.LessonSaveRequest;
import ru.asmisloff.studyplatform.dto.LessonUpdateRequest;
import ru.asmisloff.studyplatform.entity.Lesson;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.asmisloff.studyplatform.repository.CourseRepository;
import ru.asmisloff.studyplatform.repository.LessonRepository;
import ru.asmisloff.studyplatform.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public Lesson getById(long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public Lesson save(LessonSaveRequest request, long userId) {
        request.validate(courseRepository::isExists).throwIfNotEmpty();
        var user = userRepository.getById(userId);
        var course = courseRepository.getById(request.courseId());
        var lesson = new Lesson(request.title(), request.description(), request.index(), user, course);
        return lessonRepository.save(lesson);
    }

    public void update(LessonUpdateRequest request) {
        var lesson = lessonRepository.findById(request.id()).orElse(null);
        request.validate(id -> lesson != null).throwIfNotEmpty();
        assert lesson != null;
        lesson.setTitle(request.title());
        lesson.setDescription(request.description());
        lesson.setIndex(request.index());
    }
}
