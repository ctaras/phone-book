package com.github.ctaras.error;

public class UserLoginAlreadyExistsException extends Exception {

    public final String login;

    public UserLoginAlreadyExistsException(String login) {
        super("User with login [" + login + "] already exists");
        this.login = login;
    }
}
