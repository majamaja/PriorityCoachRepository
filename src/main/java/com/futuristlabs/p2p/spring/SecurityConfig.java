package com.futuristlabs.p2p.spring;

import com.futuristlabs.p2p.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@EnableWebSecurity
@EnableSpringHttpSession
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int MONTH = 60 * 60 * 24 * 30;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        final HeaderHttpSessionStrategy sessionStrategy = new HeaderHttpSessionStrategy();
        sessionStrategy.setHeaderName("Authorization");
        return sessionStrategy;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
        return authentication -> {
            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("User cannot be authneticated");
            }

            return authentication;
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new CORSFilter(), SessionManagementFilter.class)
                .formLogin().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().authorizeRequests()

                // CORS
                .regexMatchers(OPTIONS, ".*").permitAll()

                .regexMatchers("/v1/test").permitAll()
                .regexMatchers("/v1/login").permitAll()
                .regexMatchers("/v1/register").permitAll()
                .regexMatchers("/v1/reset-password").permitAll()
                .regexMatchers("/v1/sync/reference").permitAll()
                .regexMatchers("/admin/.*").permitAll()

                // swagger
                .antMatchers("/v2/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()

                // default
                .regexMatchers("/.*").authenticated();
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver();
    }

    @Bean
    public SessionRepository<ExpiringSession> sessionRepository() {
        final MapSessionRepository repository = new MapSessionRepository();
        repository.setDefaultMaxInactiveInterval(MONTH);

        return new Base64SessionRepository(repository);
    }

    public static class Base64SessionRepository implements SessionRepository<ExpiringSession> {
        private final SessionRepository<ExpiringSession> wrapped;

        public Base64SessionRepository(final SessionRepository<ExpiringSession> repository) {
            this.wrapped = repository;
        }

        @Override
        public ExpiringSession createSession() {
            return wrapped.createSession();
        }

        @Override
        public void save(final ExpiringSession session) {
            wrapped.save(session);
        }

        @Override
        public ExpiringSession getSession(final String id) {
            return wrapped.getSession(decodeSessionId(id));
        }

        @Override
        public void delete(final String id) {
            wrapped.delete(decodeSessionId(id));
        }

        private String decodeSessionId(final String id) {
            final String token = id.replace("Basic: ", "");
            return Utils.base64decode(token).trim();
        }
    }
}
