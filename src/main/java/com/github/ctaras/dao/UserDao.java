package com.github.ctaras.dao;

import com.github.ctaras.domain.User;
import com.github.ctaras.error.UserLoginAlreadyExistsException;

import java.util.List;

public interface UserDao {
    List<User> findAll();

    User findById(String id);

    User save(User user) throws UserLoginAlreadyExistsException;

    boolean isNew(User user);

    User findByLogin(String login);
}
