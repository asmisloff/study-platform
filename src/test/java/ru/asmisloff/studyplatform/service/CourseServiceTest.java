package ru.asmisloff.studyplatform.service;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.asmisloff.studyplatform.dto.CourseRequestToCreate;
import ru.asmisloff.studyplatform.dto.CourseRequestToUpdate;
import ru.asmisloff.studyplatform.entity.Course;
import ru.asmisloff.studyplatform.entity.StudentToCourseId;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.repository.CourseRepository;
import ru.asmisloff.studyplatform.repository.StudentToCourseRepository;
import ru.asmisloff.studyplatform.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseServiceTest {

    private List<Course> courses;
    private User user;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentToCourseRepository studentToCourseRepository;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void init() {
        user = userRepository.save(
            new User("user", "password", "Ivan", "Ivanoff", "i.ivanoff@mail.ru")
        );
        courses = courseRepository.saveAll(List.of(
            new Course("Война и мир", "Роман-эпопея", user),
            new Course("Евгений Онегин", "Роман в стихах", user)
        ));
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "во  |  0",
            "евг |  1",
            "q   | -1",
            "    |   "
        }
    )
    @Transactional
    void findAllByTitleWithPrefix(@Nullable String prefix, @Nullable Integer index) {
        List<Course> actual = courseService.findAllByTitleWithPrefix(prefix);
        if (index == null) {
            assertArrayEquals(courses.toArray(), actual.toArray());
        } else if (index < 0) {
            assertTrue(actual.isEmpty());
        } else {
            assertEquals(1, actual.size());
            assertEquals(courses.get(index), actual.get(0));
        }
    }

    @Test
    @Transactional
    void save() {
        String title = "Фыв ";
        CourseRequestToCreate request = new CourseRequestToCreate(title);
        var ent = courseService.save(request, user.getId());
        assertEquals(user, ent.getCreatedUser());
        assertEquals(StringUtils.trim(title), ent.getTitle());
    }

    @Test
    @Transactional
    void update() {
        Course course = courses.get(0);
        String newTitle = course.getTitle() + 1;
        CourseRequestToUpdate request = new CourseRequestToUpdate(course.getId(), newTitle);
        courseService.update(request);
        assertEquals(newTitle, course.getTitle());
    }

    @Test
    @Transactional
    void delete() {
    }

    @Test
    @Transactional
    void addStudent() {
        var course = courses.get(0);
        userService.subscribeToCourse(user.getId(), course.getId());
        em.flush();
        em.clear();
        var fetchedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(fetchedUser.relatedCourses().anyMatch(c -> c.getId().equals(course.getId())));
        var fetchedCourse = courseRepository.findById(course.getId()).orElseThrow();
        assertTrue(fetchedCourse.students().anyMatch(s -> s.getId().equals(user.getId())));
        var stc = studentToCourseRepository.findById(new StudentToCourseId(user, course)).orElseThrow();
        assertNotNull(stc.getStartTime());
    }
}