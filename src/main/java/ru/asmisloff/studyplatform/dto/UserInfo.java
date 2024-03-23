package ru.asmisloff.studyplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.asmisloff.studyplatform.entity.Role;
import ru.asmisloff.studyplatform.entity.RoleName;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;

import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {

    private final Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private OffsetDateTime registrationTime;
    private List<RoleName> roles;

    public static UserInfo from(User user) {
        return new UserInfo(
            user.getId(),
            user.getLogin(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getRegistrationTime(),
            user.getRoles().stream().map(Role::getName).toList()
        );
    }

    public AbstractViolation validate(boolean idRequired, Function<String, Boolean> isLoginUnique) {
        var v = useConstraints(this, "Пользователь", true);
        v.wrap(useConstraints(login, "Логин", true, 5, 15)
            .addRule("Должен быть уникальным", isLoginUnique.apply(login))
        );
        return v;
    }
}
