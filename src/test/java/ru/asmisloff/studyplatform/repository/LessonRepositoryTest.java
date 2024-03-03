package ru.asmisloff.studyplatform.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.asmisloff.studyplatform.entity.*;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentToCourseRepository studentToCourseRepository;

    private final String login = "login";
    private final String password = "password";
    private final String firstName = "firstName";
    private final String lastName = "lastName";
    private final String email = "email";
    private final String title = "Title";
    private final String description = "Description";

    private User user = new User(login, password, firstName, lastName, email);
    private Course course = new Course(title, description, null);
    private Lesson paragraph = new Lesson(title, description, 0, null, course);
    private Lesson lesson = new Lesson(title, description, 1, null, course);

    @BeforeEach
    public void init() {
        user = userRepository.save(user);
        paragraph.addNested(lesson);
        lesson.setCreatedUser(user);
        paragraph.setCreatedUser(user);
        course.setCreatedUser(user);
        course.addLesson(paragraph);
        course.addLesson(lesson);

        course = courseRepository.save(course);
        user.getRelatedCourses().add(course);
        userRepository.save(user);
    }

    @Test
    @Transactional
    public void save() {
        paragraph = course.sortedLessons()
                .filter(Lesson::hasNested)
                .findAny().orElseThrow();
        lesson = paragraph.getNested().get(0);

        assertNotNull(course.getId());
        assertNotNull(paragraph.getId());
        assertNotNull(lesson.getId());
        assertEquals(paragraph.getId(), lesson.getContainedLesson().getId());
        assertEquals(course.getId(), paragraph.getCourse().getId());

        List<StudentToCourse> studentToCourseList = studentToCourseRepository.findAll();
        assertFalse(studentToCourseList.isEmpty());
        assertEquals(course, studentToCourseList.get(0).getId().getCourse());
    }

    @Test
    @Transactional
    public void findById() {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        var savedCourse = assertDoesNotThrow(() -> courseRepository.findById(course.getId()).get());
        Lesson savedParagraph = savedCourse.sortedLessons().findFirst().orElseThrow();
        Lesson savedLesson = savedParagraph.getNested().get(0);
        assertEquals(course, savedCourse);
        assertEquals(paragraph, savedParagraph);
        assertEquals(lesson, savedLesson);
    }

    @Test
    @Transactional
    public void delete() {
        StudentToCourseId studentToCourseId = new StudentToCourseId(user, course);
        var studentToCourse = assertDoesNotThrow(() -> studentToCourseRepository.getById(studentToCourseId));
        assertDoesNotThrow(() -> lessonRepository.getById(paragraph.getId()));
        assertDoesNotThrow(() -> lessonRepository.getById(lesson.getId()));
        assertDoesNotThrow(() -> courseRepository.getById(course.getId()));

        studentToCourseRepository.delete(studentToCourse);
        courseRepository.delete(course);

        assertTrue(courseRepository.findById(course.getId()).isEmpty());
        assertTrue(lessonRepository.findById(paragraph.getId()).isEmpty());
        assertTrue(lessonRepository.findById(lesson.getId()).isEmpty());
        assertTrue(studentToCourseRepository.findById(studentToCourseId).isEmpty());
    }
}