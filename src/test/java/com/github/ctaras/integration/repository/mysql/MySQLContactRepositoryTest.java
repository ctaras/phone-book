package com.github.ctaras.integration.repository.mysql;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.repository.ContactRepository;
import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.repository.UserRepository;
import com.github.ctaras.integration.repository.Util;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@ActiveProfiles("mysql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLContactRepositoryTest {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DataStruct dataStruct;
    @Autowired
    Util util;

    private final String tesUsertId = "1c60344d-a403-4873-a141-ec4799731b03";
    private final String testContactId = "1060344d-a403-4873-a141-ec4799731b10";

    private User testUser;

    private void deleteAll() {
        if (countRows() > 0) {
            deleteFromTables(jdbcTemplate, dataStruct.USER_TABLE);
            deleteFromTables(jdbcTemplate, dataStruct.CONTACT_TABLE);
        }
    }

    private int countRows() {
        return countRowsInTable(jdbcTemplate, dataStruct.CONTACT_TABLE);
    }

    private Contact getTestContact() {
        return util.getContact(testUser);
    }

    @Before
    public void setUp() throws SQLException {
        deleteAll();

        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
                new ClassPathResource("com/github/ctaras/integration/repository/mysql/data.sql"));

        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
                new ClassPathResource("com/github/ctaras/integration/repository/mysql/data-contact.sql"));

        testUser = userRepository.findById(tesUsertId);
    }

    @After
    public void tearDown() {
        deleteAll();
    }

    @Test
    public void findAllByUser() {
        List<Contact> list = contactRepository.findAllByUser(testUser);
        assertThat(list, notNullValue());
        assertThat(list.size(), is(countRows()));
    }

    @Test
    public void insert() {
        int count = countRows();
        Contact contact = contactRepository.insert(getTestContact());
        assertThat(contact, notNullValue());
        assertThat(countRows(), is(count + 1));
    }

    @Test
    public void update() {
        Contact contact = contactRepository.findById(testContactId);
        final String newLastName = "newLastName";

        contact.setLastName(newLastName);
        contactRepository.update(contact);
        contact = contactRepository.findById(testContactId);

        assertThat(contact, notNullValue());
        assertThat(contact.getLastName(), is(newLastName));
    }

    @Test
    public void findById() {
        Contact contact = contactRepository.findById(testContactId);
        assertThat(contact, notNullValue());
    }

    @Test
    public void delete() {
        int count = countRows();
        Contact contact = contactRepository.findById(testContactId);
        contactRepository.delete(contact);
        assertThat(countRows(), is(count - 1));
    }

    @Test(expected = NullPointerException.class)
    public void deleteNull() {
        contactRepository.delete(null);
    }

}