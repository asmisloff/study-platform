package ru.asmisloff.studyplatform.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.asmisloff.studyplatform.entity.RoleName;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and().authorizeRequests()
            .antMatchers("/admin/**").hasRole(RoleName.ROLE_ADMIN.withoutPrefix())
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and().formLogin()
            .and().httpBasic();
    }
}
