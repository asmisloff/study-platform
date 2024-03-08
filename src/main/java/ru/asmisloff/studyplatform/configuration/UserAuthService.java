package ru.asmisloff.studyplatform.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.asmisloff.studyplatform.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        var authorities = user.getRoles().stream().map(role ->
            new SimpleGrantedAuthority(role.getNameAsString())
        ).toList();
        return new UserPrincipal(user.getId(), user.getLogin(), user.getPassword(), authorities);
    }
}
