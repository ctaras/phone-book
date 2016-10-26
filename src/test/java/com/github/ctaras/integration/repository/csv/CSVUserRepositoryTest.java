package com.github.ctaras.integration.repository.csv;

import com.github.ctaras.domain.User;
import com.github.ctaras.integration.repository.Util;
import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.repository.UserRepository;
import com.github.ctaras.repository.csv.CSVConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@ActiveProfiles("csv")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CSVUserRepositoryTest {

    private final String testId = "1c60344d-a403-4873-a141-ec4799731b03";
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DataStruct dataStruct;
    @Autowired
    CSVConfig config;
    @Autowired
    Util util;

    private User getTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin("loginAA");
        user.setPassword("123123");
        user.setFullName("FUll Name");

        return user;
    }

    private int countRows() throws IOException {
        return util.countRowsCSV(config.getUserTablePath());
    }

    private void fillDatabase() throws IOException {
        util.fillDatabaseCSV("com/github/ctaras/integration/repository/csv/data.csv", config.getUserTablePath());
    }

    @Before
    public void setUp() throws IOException {
        fillDatabase();
    }

    @Test
    public void findAll() throws IOException {
        List<User> users = userRepository.findAll();
        assertThat(users, notNullValue());
        assertThat(users.size(), is(countRows()));
    }

    @Test
    public void insert() throws IOException {
        int count = countRows();
        User user = userRepository.insert(getTestUser());
        userRepository.findAll();
        assertThat(user, notNullValue());
        assertThat(countRows(), is(count + 1));
    }

    @Test
    public void update() {
        User user = userRepository.findById(testId);
        final String newLogin = "NewLogin";

        user.setLogin(newLogin);
        userRepository.update(user);
        user = userRepository.findById(testId);

        userRepository.findAll();

        assertThat(user, notNullValue());
        assertThat(user.getLogin(), is(newLogin));
    }

    @Test
    public void findById() {
        User user = userRepository.findById(testId);
        assertThat(user, notNullValue());
    }

    @Test
    public void findByLogin() {
        User user = userRepository.findByLogin("loginA");
        assertThat(user, notNullValue());
    }
}