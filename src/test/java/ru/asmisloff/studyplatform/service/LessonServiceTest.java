package ru.asmisloff.studyplatform.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.asmisloff.studyplatform.dto.LessonSaveRequest;
import ru.asmisloff.studyplatform.dto.LessonUpdateRequest;
import ru.asmisloff.studyplatform.entity.Course;
import ru.asmisloff.studyplatform.entity.Lesson;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.repository.CourseRepository;
import ru.asmisloff.studyplatform.repository.LessonRepository;
import ru.asmisloff.studyplatform.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LessonServiceTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    private Course course;
    private Lesson lesson;
    private User user;

    @BeforeEach
    void init() {
        user = new User("login", "password", "firstName", "lastName", "email@host.net");
        user = userRepository.save(user);
        course = new Course("Title", "Description", user);
        course = courseRepository.save(course);
        lesson = new Lesson("Title", "description", 0, user, course);
        lesson = lessonRepository.save(lesson);
        em.clear();
    }

    @Test
    @Transactional
    void getById() {
        Lesson fetchedLesson = lessonService.getById(lesson.getId());
        assertEquals(lesson.getId(), fetchedLesson.getId());
    }

    @Test
    @Transactional
    void save() {
        LessonSaveRequest request = new LessonSaveRequest("Lesson1", "Description1", course.getId(), 1);
        Lesson lesson1 = lessonService.save(request, user.getId());
        Course updatedCourse = courseRepository.findById(course.getId()).orElseThrow();
        List<Lesson> courseLessons = updatedCourse.sortedLessons().toList();

        assertEquals(lesson.getId(), courseLessons.get(0).getId());
        assertEquals(lesson1.getId(), courseLessons.get(1).getId());
    }

    @Test
    @Transactional
    void update() {
        String title = lesson.getTitle() + lesson.getTitle();
        String description = lesson.getDescription() + lesson.getDescription();
        int index = lesson.getIndex() + 1;
        LessonUpdateRequest request = new LessonUpdateRequest(lesson.getId(), title, description, index);
        lessonService.update(request);
        Lesson justSaved = lessonService.getById(lesson.getId());
        assertEquals(title, justSaved.getTitle());
        assertEquals(description, justSaved.getDescription());
        assertEquals(index, justSaved.getIndex());
    }
}