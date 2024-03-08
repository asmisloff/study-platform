package ru.asmisloff.studyplatform.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.asmisloff.studyplatform.configuration.UserPrincipal;
import ru.asmisloff.studyplatform.controller.Endpoint;
import ru.asmisloff.studyplatform.controller.request.PagingAndSearchParameters;
import ru.asmisloff.studyplatform.controller.request.filtering.AdminUserFilteringCriteria;
import ru.asmisloff.studyplatform.controller.request.parameter.DatabaseID;
import ru.asmisloff.studyplatform.dto.Paginated;
import ru.asmisloff.studyplatform.dto.UserInfo;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.asmisloff.studyplatform.repository.UserRepository;
import ru.asmisloff.studyplatform.service.EmailService;
import ru.asmisloff.studyplatform.service.UserService;

import static ru.asmisloff.studyplatform.entity.Resource.USER;

@RestController
@RequestMapping(Endpoint.ADMIN_USER)
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/{id}")
    public UserInfo getById(@PathVariable DatabaseID id) {
        id.validate("id", true, false).throwIfNotEmpty();
        User user = userRepository.findById(id.longValue()).orElseThrow(() ->
            new ResourceNotFoundException(USER, id.longValue())
        );
        return UserInfo.fromUser(user);
    }

    @GetMapping
    public Paginated<UserInfo> getPage(
        PagingAndSearchParameters pagingAndSearchParameters,
        AdminUserFilteringCriteria filteringCriteria,
        Authentication auth
    ) {
        filteringCriteria.validate(pagingAndSearchParameters).throwIfNotEmpty();
        Page<UserInfo> page = userService
            .findAll(pagingAndSearchParameters, filteringCriteria)
            .map(UserInfo::fromUser);
        emailService.sendCredentials("", "", "");
        return Paginated.from(page);
    }

    @PostMapping
    public long create(@RequestBody UserInfo userInfo) {
        return userService.create(userInfo).getId();
    }

    @PutMapping
    public void update(@RequestBody UserInfo userInfo, Authentication auth) {
        userService.update(userInfo, (UserPrincipal) auth.getPrincipal());
    }
}
