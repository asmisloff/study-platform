package ru.asmisloff.studyplatform.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asmisloff.studyplatform.configuration.UserPrincipal;
import ru.asmisloff.studyplatform.controller.request.filtering.admin.AdminUserFilteringCriteria;
import ru.asmisloff.studyplatform.controller.request.filtering.admin.AdminUserPaginationParameters;
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

    @Value("${study.platform.random.password.length}")
    private int randomPasswordLength;

    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER, id));
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfoById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER, id));
        return UserInfo.from(user);
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(AdminUserPaginationParameters p, AdminUserFilteringCriteria cr) {
        return userRepository.findAll(p.pageable());
    }

    @Transactional
    public User createAndSendCredentials(UserInfo userInfo) {
        String password = RandomStringUtils.randomAlphabetic(randomPasswordLength);
        User user = create(userInfo, password);
        emailService.sendCredentials(user.getEmail(), user.getLogin(), password);
        return user;
    }

    @Transactional
    public User create(UserInfo userInfo, String password) {
        String login = userInfo.getLogin();
        String encodedPassword = passwordEncoder.encode(password);
        String firstName = userInfo.getFirstName();
        String lastName = userInfo.getLastName();
        String email = userInfo.getEmail();
        User user = new User(login, encodedPassword, firstName, lastName, email);
        return userRepository.save(user);
    }

    @Transactional
    public void update(UserInfo userInfo, UserPrincipal principal) {
        userInfo.validate(true, (id) -> true).throwIfNotEmpty();
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
        var course = courseRepository.getReferenceById(courseId);
        user.subscribeToCourse(course);
        userRepository.save(user);
    }
}
