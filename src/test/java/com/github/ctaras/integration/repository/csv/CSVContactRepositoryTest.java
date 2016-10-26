package com.github.ctaras.integration.repository.csv;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.integration.repository.Util;
import com.github.ctaras.repository.ContactRepository;
import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.repository.UserRepository;
import com.github.ctaras.repository.csv.CSVConfig;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@ActiveProfiles("csv")
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CSVContactRepositoryTest {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    DataStruct dataStruct;
    @Autowired
    CSVConfig config;
    @Autowired
    Util util;

    private final String tesUsertId = "1c60344d-a403-4873-a141-ec4799731b03";
    private final String testContactId = "1060344d-a403-4873-a141-ec4799731b10";

    private User testUser;

    private Contact getTestContact() {
        return util.getContact(testUser);
    }

    private int countRows() throws IOException {
        return util.countRowsCSV(config.getContactTablePath());
    }

    private void fillDatabase() throws IOException {
        util.fillDatabaseCSV("com/github/ctaras/integration/repository/csv/data.csv",
                config.getUserTablePath());
        util.fillDatabaseCSV("com/github/ctaras/integration/repository/csv/data-contact.csv",
                config.getContactTablePath());
    }

    @Before
    public void setUp() throws IOException {
        fillDatabase();
        testUser = userRepository.findById(tesUsertId);
    }

    @Test
    public void findAllByUser() throws IOException {
        List<Contact> list = contactRepository.findAllByUser(testUser);
        assertThat(list, notNullValue());
        assertThat(list.size(), is(countRows()));
    }

    @Test
    public void insert() throws IOException {
        int count = countRows();
        Contact contact = contactRepository.insert(getTestContact());
        contactRepository.findAllByUser(testUser);
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
        contactRepository.findAllByUser(testUser);
        assertThat(contact, notNullValue());
        assertThat(contact.getLastName(), is(newLastName));
    }

    @Test
    public void findById() {
        Contact contact = contactRepository.findById(testContactId);
        assertThat(contact, notNullValue());
    }

    @Test
    public void delete() throws IOException {
        int count = countRows();
        Contact contact = contactRepository.findById(testContactId);
        contactRepository.delete(contact);
        contactRepository.findAllByUser(testUser);
        assertThat(countRows(), is(count - 1));
    }

    @Test(expected = NullPointerException.class)
    public void deleteNull() {
        contactRepository.delete(null);
        contactRepository.findAllByUser(testUser);
    }
}