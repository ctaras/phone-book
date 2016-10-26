package com.github.ctaras.repository.csv;

import com.github.ctaras.domain.User;
import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.repository.UserRepository;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Profile("csv")
@Repository
public class CSVUserRepository extends CSVTemplate
        implements UserRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(CSVUserRepository.class);

    @Autowired
    public CSVUserRepository(CSVConfig config, DataStruct dt) {
        super(config, dt);
    }

    @Override
    public List<User> findAll() {

        List<User> list = new ArrayList<>();

        Connection conn = null;

        try {

            Statement stmt;
            ResultSet results;

            lock.readLock().lock();

            try {
                conn = config.getConnection();

                stmt = conn.createStatement();
                results = stmt.executeQuery("SELECT user_id, login, password, full_name FROM user");
            } finally {
                lock.readLock().unlock();
            }

            while (results.next()) {
                User user = new User();
                user.setId(results.getString(dt.USER_ID));
                user.setLogin(results.getString(dt.USER_LOGIN));
                user.setPassword(results.getString(dt.USER_PASSWD));
                user.setFullName(results.getString(dt.USER_FULL_NAME));
                list.add(user);
            }

            stmt.close();
        } catch (SQLException e) {
            Log.e(logger, () -> "Error of reading user database", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Log.e(logger, () -> "Error of close connection user database", e);
                }
            }
        }

        return list;
    }

    @Override
    public User insert(User user) {
        if (!appendData(config.getUserTablePath(), getUserRow(user),
                logger, "Error of writing user database")) {
            return null;
        }

        return user;
    }

    @Override
    public User update(User user) {
        String id = user.getId();
        Path filePath = Paths.get(config.getUserTablePath());

        if (!updateData(filePath, id, getUserRow(user), logger, "Error of writing user database")) {
            return null;
        }

        return user;
    }

    @Override
    public User findById(String id) {
        return findUserByParam("SELECT user_id, login, password, full_name FROM user " +
                "WHERE user_id = ? LIMIT 1", id);
    }

    @Override
    public User findByLogin(String login) {
        return findUserByParam("SELECT user_id, login, password, full_name FROM user " +
                "WHERE login = ? LIMIT 1", login);
    }

    private User findUserByParam(String query, String param) {
        Connection conn = null;
        User user = null;

        try {
            PreparedStatement stmt;
            ResultSet results;
            lock.readLock().lock();
            try {
                conn = config.getConnection();

                stmt = conn.prepareStatement(query);
                stmt.setString(1, param);
                results = stmt.executeQuery();
            } finally {
                lock.readLock().unlock();
            }

            if (results.next()) {
                user = new User();
                user.setId(results.getString(dt.USER_ID));
                user.setLogin(results.getString(dt.USER_LOGIN));
                user.setPassword(results.getString(dt.USER_PASSWD));
                user.setFullName(results.getString(dt.USER_FULL_NAME));
            }
            stmt.close();
        } catch (SQLException e) {
            Log.e(logger, () -> "Error of reading user database", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Log.e(logger, () -> "Error of close connection user database", e);
                }
            }
        }

        return user;
    }

    private String getUserRow(User user) {
        return new StringBuilder()
                .append(value(user.getId()))
                .append(config.columnSeparator)
                .append(value(user.getLogin()))
                .append(config.columnSeparator)
                .append(value(user.getPassword()))
                .append(config.columnSeparator)
                .append(value(user.getFullName()))
                .toString();
    }

    private String value(String value) {
        return value == null ? "" : value;
    }
}
