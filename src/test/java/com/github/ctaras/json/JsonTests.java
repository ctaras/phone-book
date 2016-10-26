package com.github.ctaras.json;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.dto.ContactList;
import com.github.ctaras.validation.ValidationError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class JsonTests {

    @Autowired
    private JacksonTester<User> jsonUser;

    @Autowired
    private JacksonTester<Contact> jsonContact;

    @Autowired
    private JacksonTester<ContactList> jsonContactList;

    @Autowired
    private JacksonTester<ValidationError> jsonValidationError;

    @Test
    public void testSerializeUser() throws Exception {
        User user = new User();
        user.setId("1c60344d-a403-4873-a141-ec4799731b03");
        user.setLogin("IvanD");
        user.setPassword("123456");
        user.setFullName("Ivan ivanovich");

        assertThat(jsonUser.write(user)).isEqualToJson("userExpected.json");
    }

    @Test
    public void testSerializeContact() throws Exception {
        Contact contact = getContact();
        assertThat(jsonContact.write(contact)).isEqualToJson("contactExpected.json");
    }

    @Test
    public void testSerializeContactList() throws Exception {

        Contact contact = getContact();

        List<Contact> listModel = new ArrayList<>();
        listModel.add(contact);
        listModel.add(contact);

        ContactList list = new ContactList(listModel);
        assertThat(jsonContactList.write(list)).isEqualToJson("contactListExpected.json");
    }

    @Test
    public void testSerializeError() throws Exception {

        ValidationError error = new ValidationError("Validation failed. 2 error(s)");
        error.addValidationError("username",  "Username is null");
        error.addValidationError("password",  "Password is null");

        assertThat(jsonValidationError.write(error)).isEqualToJson("errorListExpected.json");
    }

    private Contact getContact() {
        Contact contact = new Contact();
        contact.setId("1060344d-a403-4873-a141-ec4799731b10");
        contact.setLastName("Last name");
        contact.setFirstName("First name");
        contact.setMiddleName("Middle name");
        contact.setMobilePhoneNumber("380991234567");
        contact.setHomePhoneNumber("380991234567");
        contact.setAddress("Address 1");
        contact.setEmail("mail@gmail.com");
        contact.setUserId("1c60344d-a403-4873-a141-ec4799731b03");
        return contact;
    }
}
