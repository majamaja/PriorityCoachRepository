package com.futuristlabs.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

import static com.futuristlabs.utils.crypto.CryptoUtils.encodePassword;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableSpringHttpSession
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private  static final int MONTH = 60 * 60 * 24 * 30;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new CORSFilter(), SessionManagementFilter.class)
                .formLogin().disable()
                .csrf().disable()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()

                // CORS
                .regexMatchers(OPTIONS, ".*").permitAll()

                // common
                .regexMatchers(GET, "/status").permitAll()
                .and()

                // swagger
                .authorizeRequests()
                .antMatchers("/v2/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
                .and()

                .authorizeRequests().anyRequest().authenticated();
    }

    @Autowired
    public void configurationGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return encodePassword(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String passToCompare = encodePassword(rawPassword);
                return passToCompare.equals(encodedPassword);
            }
        });
    }

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new RequestOrHeaderHttpSessionStrategyAdapter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver();
    }

    @Bean
    public SessionRepository<ExpiringSession> sessionRepository() {
        MapSessionRepository repository = new MapSessionRepository();
        repository.setDefaultMaxInactiveInterval(MONTH);
        return repository;
    }
}