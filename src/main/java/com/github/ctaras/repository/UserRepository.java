package com.github.ctaras.repository;

import com.github.ctaras.domain.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User insert(User user);

    User update(User user);

    User findById(String id);

    User findByLogin(String login);
}
