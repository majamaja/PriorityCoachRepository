package com.futuristlabs.p2p.spring;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcAutoConfiguration {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return new CustomObjectMapper();
    }
}
