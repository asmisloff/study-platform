package ru.asmisloff.studyplatform.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.asmisloff.studyplatform.configuration.UserPrincipal;
import ru.asmisloff.studyplatform.controller.request.filtering.admin.AdminUserFilteringCriteria;
import ru.asmisloff.studyplatform.controller.request.filtering.admin.AdminUserPaginationParameters;
import ru.asmisloff.studyplatform.dto.UserInfo;
import ru.asmisloff.studyplatform.entity.Course;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.repository.CourseRepository;
import ru.asmisloff.studyplatform.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.asmisloff.studyplatform.entity.RoleName.ROLE_ADMIN;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void getUserById() {
        User user = userService.getUserById(1L);
        assertEquals(1L, user.getId());
        assertEquals("admin", user.getLogin());
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals(ROLE_ADMIN)));
    }

    @Test
    @Transactional
    void getUserInfoById() {
        UserInfo userInfo = userService.getUserInfoById(1L);
        assertEquals(1L, userInfo.getId());
        assertEquals("admin", userInfo.getLogin());
        assertTrue(userInfo.getRoles().contains(ROLE_ADMIN));
    }

    @Test
    @Transactional
    void findAll() {
        throw new NotImplementedException();
    }

    @Test
    @Transactional
    void create() {
        User savedUser = createAndGetNewUser();
        List<User> dbUsers = findAllActiveUsers();
        assertTrue(dbUsers.contains(savedUser));
    }

    @Test
    @Transactional
    void update() {
        UserInfo userInfo = UserInfo.from(createAndGetNewUser());
        String updatedLogin = userInfo.getLogin() + 1;
        userInfo.setLogin(updatedLogin);
        userService.update(userInfo, Mockito.mock(UserPrincipal.class));
        em.flush();
        em.clear();
        List<User> dbUsers = findAllActiveUsers();
        assertTrue(dbUsers.stream().anyMatch(u -> u.getLogin().equals(updatedLogin)));
    }

    @Test
    @Transactional
    void subscribeToCourse() {
        User user = randomUser();
        user = userService.create(UserInfo.from(user), user.getPassword());
        Course course = courseRepository.save(CourseServiceTest.randomCourse(user));
        userService.subscribeToCourse(user.getId(), course.getId());
        em.flush();
        em.clear();
        /* Со стороны пользователя */
        user = userRepository.findById(user.getId()).orElseThrow();
        Set<Course> relatedCourses = user.relatedCourses().collect(Collectors.toSet());
        assertEquals(1, relatedCourses.size());
        assertTrue(relatedCourses.contains(course));
        /* Со стороны курса */
        course = courseRepository.findById(course.getId()).orElseThrow();
        Set<User> students = course.students().collect(Collectors.toSet());
        assertEquals(1, students.size());
        assertTrue(students.contains(user));
    }

    public static User randomUser() {
        String login = random(10, true, true);
        String password = random(15, true, true);
        String firstName = randomAlphabetic(8);
        String lastName = randomAlphabetic(8);
        String email = random(8, true, true) + '@' + randomAlphabetic(5) + ".net";
        return new User(login, password, firstName, lastName, email);
    }

    private User createAndGetNewUser() {
        UserInfo userInfo = UserInfo.from(randomUser());
        return userService.create(userInfo, "password1");
    }

    @NotNull
    private List<User> findAllActiveUsers() {
        return userService.findAll(
            AdminUserPaginationParameters.unpaged(),
            AdminUserFilteringCriteria.undefined()
        ).getContent();
    }
}