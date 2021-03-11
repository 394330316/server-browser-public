package com.browser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"com.browser.config","com.browser.controller","com.browser.data","com.browser.manager","com.browser.repository","com.browser.response","com.browser.service","com.browser.sql","com.browser.utils"})
@EntityScan("com.browser.sql")
@EnableJpaRepositories("com.browser")
@SpringBootApplication
public class BrowserApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BrowserApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BrowserApplication.class);
    }
}
