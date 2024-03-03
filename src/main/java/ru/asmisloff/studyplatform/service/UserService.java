package ru.asmisloff.studyplatform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.asmisloff.studyplatform.repository.CourseRepository;
import ru.asmisloff.studyplatform.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void subscribeToCourse(long userId, long courseId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
        var course = courseRepository.getById(courseId);
        user.subscribeToCourse(course);
        userRepository.save(user);
    }
}
