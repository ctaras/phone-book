package com.github.ctaras.dao.impl;

import com.github.ctaras.dao.ContactDao;
import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.error.UserLoginAlreadyExistsException;
import com.github.ctaras.repository.ContactRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContactDaoImplTest {

    private final String testUserId = "1c60344d-a403-4873-a141-ec4799731b03";
    private final String testContactId = "1060344d-a403-4873-a141-ec4799731b10";

    private User testUser;

    @Mock
    ContactRepository contactRepository;

    private ContactDao contactDao;

    @Before
    public void setUp() {
        contactDao = new ContactDaoImpl(contactRepository);
        testUser = new User();
        testUser.setId(UUID.randomUUID().toString());
        testUser.setLogin("Login");
        testUser.setPassword("123");
        testUser.setFullName("Full name");
    }

    private List<Contact> getTestContactList() {
        List<Contact> list = new ArrayList<>();

        list.add(getTestContact());
        list.add(getTestContact());
        list.add(getTestContact());

        return list;
    }

    private Contact getTestContact() {
        Contact contact = new Contact();
        contact.setId(testContactId);
        contact.setLastName("Last name");
        contact.setFirstName("First name");
        contact.setMiddleName("Middle name");
        contact.setMobilePhoneNumber("380991234567");
        contact.setHomePhoneNumber("380991234567");
        contact.setAddress("Address 1");
        contact.setEmail("mail@gmail.com");

        contact.setUserId(testUser.getId());

        return contact;
    }

    @Test
    public void findAllByUser() {
        when(contactRepository.findAllByUser(testUser)).thenReturn(getTestContactList());
        List<Contact> result = contactDao.findAllByUser(testUser);

        verify(contactRepository).findAllByUser(testUser);

        assertThat(result, notNullValue());
        assertThat(result.size(), is(getTestContactList().size()));
    }

    @Test
    public void findById() {
        when(contactRepository.findById(testContactId)).thenReturn(getTestContact());

        Contact contact = contactDao.findById(testContactId);
        verify(contactRepository).findById(testContactId);
        assertThat(contact, notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void save() {
        contactDao.save(null);
    }

    @Test
    public void saveExistsContact() {
        Contact contact = getTestContact();
        when(contactRepository.findById(contact.getId())).thenReturn(contact);
        when(contactRepository.update(contact)).thenReturn(contact);
        contactDao.save(contact);

        verify(contactRepository).findById(contact.getId());
        verify(contactRepository).update(contact);
    }

    @Test
    public void saveNotExistsUser() throws UserLoginAlreadyExistsException {
        Contact contact = getTestContact();
        when(contactRepository.findById(contact.getId())).thenReturn(null);
        when(contactRepository.insert(contact)).thenReturn(contact);
        contactDao.save(contact);

        verify(contactRepository).findById(contact.getId());
        verify(contactRepository).insert(contact);
    }

    @Test(expected = NullPointerException.class)
    public void isNewNull() {
        contactDao.isNew(null);
    }

    @Test
    public void isNew_1() {
        Contact contact = new Contact();
        assertThat(contactDao.isNew(contact), is(true));
    }

    @Test
    public void isNew_2() {
        Contact contact = new Contact();
        contact.setId("");
        assertThat(contactDao.isNew(contact), is(true));
    }

    @Test
    public void isNew_3() {
        when(contactRepository.findById(testContactId)).thenReturn(getTestContact());
        Contact contact = new Contact();
        contact.setId(testContactId);
        assertThat(contactDao.isNew(contact), is(false));
    }

    @Test
    public void delete() {
        Contact contact = getTestContact();
        contactDao.delete(contact);
        verify(contactRepository).delete(contact);
    }

    @Test
    public void deleteNull() {
        Contact contact = null;
        contactDao.delete(contact);
        verify(contactRepository, never()).delete(contact);
    }

    @Test
    public void deleteEmptyContact() {
        Contact contact = new Contact();
        contactDao.delete(contact);
        verify(contactRepository, never()).delete(contact);
    }
}