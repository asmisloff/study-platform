package ru.asmisloff.studyplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import ru.asmisloff.studyplatform.entity.Role;
import ru.asmisloff.studyplatform.entity.RoleName;
import ru.asmisloff.studyplatform.entity.User;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@With
public class UserInfo {

    private final Long id;
    private final String login;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final OffsetDateTime registrationTime;
    private final List<RoleName> roles;

    public UserInfo(User user) {
        id = user.getId();
        login = user.getLogin();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        phone = user.getPhone();
        registrationTime = user.getRegistrationTime();
        roles = user.getRoles().stream().map(Role::getName).toList();
    }
}
