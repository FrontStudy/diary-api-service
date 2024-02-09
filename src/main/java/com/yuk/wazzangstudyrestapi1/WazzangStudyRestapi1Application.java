package com.yuk.wazzangstudyrestapi1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WazzangStudyRestapi1Application {

    public static void main(String[] args) {
        SpringApplication.run(WazzangStudyRestapi1Application.class, args);
    }

}
