package ru.asmisloff.studyplatform.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import ru.asmisloff.studyplatform.dto.LessonSaveRequest;
import ru.asmisloff.studyplatform.dto.LessonUpdateRequest;
import ru.asmisloff.studyplatform.entity.Course;
import ru.asmisloff.studyplatform.entity.Lesson;
import ru.asmisloff.studyplatform.entity.User;
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
        return lessonRepository.save(fromDto(request, userId));
    }

    public void update(LessonUpdateRequest request) {
        throw new NotImplementedException();
    }

    private Lesson fromDto(LessonSaveRequest request, long userId) {
        String title = request.getTitle();
        String description = request.getDescription();
        User user = userRepository.getById(userId);
        Course course = courseRepository.getById(request.getCourseId());
        int index = request.getIndex();
        return new Lesson(title, description, user, course, index);
    }
}
