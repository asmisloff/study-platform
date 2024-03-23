package ru.asmisloff.studyplatform.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.asmisloff.studyplatform.configuration.UserPrincipal;
import ru.asmisloff.studyplatform.controller.Endpoint;
import ru.asmisloff.studyplatform.controller.request.filtering.admin.AdminUserFilteringCriteria;
import ru.asmisloff.studyplatform.controller.request.filtering.admin.AdminUserPaginationParameters;
import ru.asmisloff.studyplatform.controller.request.parameter.DbIdFilteringCriteria;
import ru.asmisloff.studyplatform.dto.Paginated;
import ru.asmisloff.studyplatform.dto.UserInfo;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.asmisloff.studyplatform.repository.UserRepository;
import ru.asmisloff.studyplatform.service.UserService;
import ru.asmisloff.studyplatform.validation.Constraints;

import static ru.asmisloff.studyplatform.entity.Resource.USER;

@RestController
@RequestMapping(Endpoint.ADMIN_USER)
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/{id}")
    public UserInfo getById(@PathVariable DbIdFilteringCriteria id) {
        id.validate("id", true, false).throwIfNotEmpty();
        User user = userRepository.findById(id.toBigInt()).orElseThrow(() ->
            new ResourceNotFoundException(USER, id.toBigInt())
        );
        return UserInfo.from(user);
    }

    @GetMapping
    public Paginated<UserInfo> getPage(
        AdminUserPaginationParameters paginationParameters,
        AdminUserFilteringCriteria filteringCriteria,
        Authentication auth
    ) {
        Constraints.useConstraints(this, "Запрос данных пользователя", true)
            .wrap(paginationParameters.validate())
            .wrap(filteringCriteria.validate())
            .throwIfNotEmpty();
        Page<UserInfo> page = userService
            .findAll(paginationParameters, filteringCriteria)
            .map(UserInfo::from);
        return Paginated.from(page);
    }

    @PostMapping
    public long create(@RequestBody UserInfo userInfo) {
        return userService.createAndSendCredentials(userInfo).getId();
    }

    @PutMapping
    public void update(@RequestBody UserInfo userInfo, Authentication auth) {
        userService.update(userInfo, (UserPrincipal) auth.getPrincipal());
    }
}
