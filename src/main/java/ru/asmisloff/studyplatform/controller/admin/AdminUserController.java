package ru.asmisloff.studyplatform.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.asmisloff.studyplatform.controller.Endpoint;
import ru.asmisloff.studyplatform.controller.request.parameter.DatabaseID;
import ru.asmisloff.studyplatform.dto.UserInfo;
import ru.asmisloff.studyplatform.entity.Resource;
import ru.asmisloff.studyplatform.entity.User;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;
import ru.asmisloff.studyplatform.repository.UserRepository;

@RestController
@RequestMapping(Endpoint.ADMIN_USER)
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public UserInfo getById(@PathVariable DatabaseID id) {
        id.validate("id", true, false).throwIfNotEmpty();
        User user = userRepository.findById(id.longValue()).orElseThrow(() ->
            new ResourceNotFoundException(Resource.COURSE, id.longValue()));
        return new UserInfo(user);
    }
}
