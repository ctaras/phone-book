package com.github.ctaras;

import com.github.ctaras.error.UserLoginAlreadyExistsException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhoneBookApplication {

    public static void main(String[] args) throws UserLoginAlreadyExistsException {
        SpringApplication.run(PhoneBookApplication.class, args);
    }
}
