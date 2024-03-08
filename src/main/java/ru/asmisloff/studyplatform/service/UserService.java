package ru.asmisloff.studyplatform.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asmisloff.studyplatform.configuration.UserPrincipal;
import ru.asmisloff.studyplatform.controller.request.PagingAndSearchParameters;
import ru.asmisloff.studyplatform.controller.request.filtering.AdminUserFilteringCriteria;
import ru.asmisloff.studyplatform.dto.UserInfo;
import ru.asmisloff.studyplatform.entity.RoleName;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.asmisloff.studyplatform.repository.CourseRepository;
import ru.asmisloff.studyplatform.repository.RoleRepository;
import ru.asmisloff.studyplatform.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.List;

import static ru.asmisloff.studyplatform.entity.Resource.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<User> findAll(PagingAndSearchParameters p, AdminUserFilteringCriteria cr) {
        return userRepository.findAll(cr.pageable(p));
    }

    @Transactional
    public User create(UserInfo userInfo) {
        String login = userInfo.getLogin();
        String password = RandomStringUtils.randomAlphabetic(10);
        String encodedPassword = passwordEncoder.encode(password);
        String firstName = userInfo.getFirstName();
        String lastName = userInfo.getLastName();
        String email = userInfo.getEmail();
        User user = new User(login, encodedPassword, firstName, lastName, email);
        emailService.sendCredentials(email, login, password);
        return user;
    }

    @Transactional
    public void update(UserInfo userInfo, UserPrincipal principal) {
        userInfo.validate(true).throwIfNotEmpty();
        Long id = userInfo.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER, id));

        user.setLogin(userInfo.getLogin());
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setEmail(userInfo.getEmail());
        user.setPhone(userInfo.getPhone());
        List<String> roleNames = userInfo.getRoles().stream()
            .map(RoleName::withPrefix)
            .toList();
        user.updateRoles(roleRepository.findAllByName(roleNames));
        user.setLastUpdateTime(OffsetDateTime.now());
        user.setLastUpdatedUserId(principal.getId());
        userRepository.save(user);
    }

    @Transactional
    public void subscribeToCourse(long userId, long courseId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, userId));
        var course = courseRepository.getById(courseId);
        user.subscribeToCourse(course);
        userRepository.save(user);
    }
}
