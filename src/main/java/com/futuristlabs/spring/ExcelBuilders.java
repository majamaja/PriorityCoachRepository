package com.futuristlabs.spring;

import com.futuristlabs.func.users.User;
import com.futuristlabs.utils.excel.ExcelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelBuilders {

    @Bean
    public ExcelBuilder<User> userExcelBuilder() {
        return new ExcelBuilder<User>()
                .with("ID", User::getId)
                .with("Name", User::getName)
                .with("Email", User::getEmail)
                .withDateTime("Created At", User::getCreatedAt)
                .withDateTime("Last Login", User::getLastLogin);
    }
}
