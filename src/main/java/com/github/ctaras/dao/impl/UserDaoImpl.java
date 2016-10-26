package com.github.ctaras.dao.impl;

import com.github.ctaras.dao.UserDao;
import com.github.ctaras.domain.User;
import com.github.ctaras.error.UserLoginAlreadyExistsException;
import com.github.ctaras.repository.UserRepository;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserDaoImpl implements UserDao {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDaoImpl.class);

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDaoImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAll() {

        Log.d(logger, () -> "Get list of all users");

        List<User> list = userRepository.findAll();

        Log.d(logger, () -> "Found " + list.size() + " users");

        return list;
    }

    @Override
    public User findById(final String id) {

        Objects.requireNonNull(id);

        Log.d(logger, () -> "Search user by id [" + id + "]...");

        User user = userRepository.findById(id);

        if (user == null) {
            Log.w(logger, () -> "User with id [" + id + "] not found");
        } else {
            Log.d(logger, () -> "Found user by id [" + id + "]: " + user);
        }

        return user;
    }

    @Override
    public User save(final User user) throws UserLoginAlreadyExistsException {

        Objects.requireNonNull(user);

        Log.d(logger, () -> "Saving user: " + user);

        User result;

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        if (isNew(user)) {
            User res = userRepository.findByLogin(user.getLogin());
            if (res != null) {
                throw new UserLoginAlreadyExistsException(user.getLogin());
            }

            if (user.getId() == null || user.getId().isEmpty()) {
                user.setId(UUID.randomUUID().toString());
                Log.d(logger, () -> "Set id: " + user.getId());
            }

            result = userRepository.insert(user);
            Log.d(logger, () -> "User insert: id = " + user.getId());
        } else {
            result = userRepository.update(user);
            Log.d(logger, () -> "User update: id = " + user.getId());
        }

        Log.d(logger, () -> "User was saved: id = " + result.getId());

        return result;
    }

    @Override
    public boolean isNew(final User user) {
        Objects.requireNonNull(user);

        return user.getId() == null ||
                user.getId().isEmpty() ||
                userRepository.findById(user.getId()) == null;
    }

    @Override
    public User findByLogin(String login) {
        Objects.requireNonNull(login);

        Log.d(logger, () -> "Search user by login [" + login + "]...");

        User user = userRepository.findByLogin(login);

        if (user == null) {
            Log.w(logger, () -> "User with login [" + login + "] not found");
        } else {
            Log.d(logger, () -> "Found user by login [" + login + "]: " + user);
        }

        return user;
    }
}
