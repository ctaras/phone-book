package com.github.ctaras.integration.repository.mysql;

import com.github.ctaras.domain.User;
import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@ActiveProfiles("mysql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLUserRepositoryTest {

    private final String testId = "1c60344d-a403-4873-a141-ec4799731b03";
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DataStruct dataStruct;

    private void deleteAll() {
        if (countRows() > 0) {
            deleteFromTables(jdbcTemplate, dataStruct.USER_TABLE);
        }
    }

    private User getTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin("loginBB");
        user.setPassword("123123");
        user.setFullName("FUll Name");

        return user;
    }

    private int countRows() {
        return countRowsInTable(jdbcTemplate, dataStruct.USER_TABLE);
    }

    @Before
    public void setUp() throws SQLException {
        deleteAll();
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
                new ClassPathResource("com/github/ctaras/integration/repository/mysql/data.sql"));
    }

    @After
    public void tearDown() {
        deleteAll();
    }

    @Test
    public void findAll() {
        List<User> users = userRepository.findAll();
        assertThat(users, notNullValue());
        assertThat(users.size(), is(countRows()));
    }

    @Test
    public void insert() {
        int count = countRows();
        User user = userRepository.insert(getTestUser());
        assertThat(user, notNullValue());
        assertThat(countRows(), is(count + 1));
    }

    @Test
    public void update() {
        User user = userRepository.findById(testId);
        String login = user.getLogin();

        final String newLogin = "NewLogin";

        user.setLogin(newLogin);
        userRepository.update(user);
        user = userRepository.findById(testId);

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