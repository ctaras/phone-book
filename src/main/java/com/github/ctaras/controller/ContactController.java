package com.github.ctaras.controller;

import com.github.ctaras.dao.ContactDao;
import com.github.ctaras.dao.UserDao;
import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.dto.ContactList;
import com.github.ctaras.util.Log;
import com.github.ctaras.validation.ValidationErrorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger =
            LoggerFactory.getLogger(ContactController.class);

    private final ContactDao contactDao;

    private final UserDao userDao;

    @Autowired
    public ContactController(ContactDao contactDao, UserDao userDao) {
        this.contactDao = contactDao;
        this.userDao = userDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ContactList list(Principal principal) {
        String login = principal.getName();

        Log.i(logger, () -> "List of all user contacts, " + login);

        User user = userDao.findByLogin(login);

        List<Contact> contacts = contactDao.findAllByUser(user);

        Log.i(logger, () -> "No. of contacts: " + contacts.size());

        return new ContactList(contacts);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@Valid Contact contact, Errors errors, Principal principal) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }

        User user = getCurrentUser(principal);

        contact.setId(null);
        contact.setUserId(user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(contactDao.save(contact));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity update(@PathVariable("id") String id, @Valid Contact contact,
                                 Errors errors, Principal principal) {
        Log.i(logger, () -> "Update contact, id = " + id);

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }

        Contact existsContact = contactDao.findById(id);
        if (!hasUserContact(contact, principal)) {
            Log.i(logger, () -> "Contact not found = " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User user = getCurrentUser(principal);

        contact.setId(existsContact.getId());
        contact.setUserId(user.getId());

        contact = contactDao.save(contact);

        return ResponseEntity.status(HttpStatus.OK).body(contact);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Contact> get(@PathVariable("id") String id, Principal principal) {
        Contact contact = contactDao.findById(id);

        Log.i(logger, () -> "Get contact, id = " + id);

        if (!hasUserContact(contact, principal)) {
            Log.i(logger, () -> "Contact not found = " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(contact);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") String id, Principal principal) {
        Contact contact = contactDao.findById(id);

        Log.i(logger, () -> "Delete contact, id = " + id);

        if (!hasUserContact(contact, principal)) {
            Log.i(logger, () -> "Contact not found = " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        contactDao.delete(contact);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    private boolean hasUserContact(Contact contact, Principal principal) {
        User user = getCurrentUser(principal);
        return !(user == null || contact == null) && user.getId().equals(contact.getUserId());
    }

    private User getCurrentUser(Principal principal) {
        User user = null;

        String username = principal.getName();
        if (username != null) {
            user = userDao.findByLogin(username);
        }

        return user;
    }
}
