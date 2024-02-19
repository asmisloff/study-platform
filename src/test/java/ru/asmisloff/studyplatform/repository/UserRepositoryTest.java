package ru.asmisloff.studyplatform.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.asmisloff.studyplatform.StudyPlatformApplication;
import ru.asmisloff.studyplatform.entity.Role;
import ru.asmisloff.studyplatform.entity.User;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = { StudyPlatformApplication.class })
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Transactional
    public void save() {
        final Role role = roleRepository.save(new Role(1L, "admin"));
        final User user = new User("login", "pwd", "fn", "ln", "email");
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertTrue(userRepository
                .findById(savedUser.getId())
                .orElseThrow()
                .getRoles()
                .contains(role));
    }
}
