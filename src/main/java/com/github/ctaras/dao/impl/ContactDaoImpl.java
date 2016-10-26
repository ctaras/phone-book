package com.github.ctaras.dao.impl;

import com.github.ctaras.dao.ContactDao;
import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.repository.ContactRepository;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ContactDaoImpl implements ContactDao {

    private static final Logger logger =
            LoggerFactory.getLogger(ContactDaoImpl.class);

    private final ContactRepository contactRepository;

    @Autowired
    public ContactDaoImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public List<Contact> findAllByUser(User user) {
        Objects.requireNonNull(user);

        Log.d(logger, () -> "Get list of all user contacts, " + user);

        List<Contact> list;

        if (user.getId() == null || user.getId().isEmpty()) {
            list = new ArrayList<>();
        } else {
            list = contactRepository.findAllByUser(user);
        }

        Log.d(logger, () -> "Found " + list.size() + " contacts");

        return list;
    }

    @Override
    public Contact findById(String id) {
        Objects.requireNonNull(id);

        Log.d(logger, () -> "Search contact by id [" + id + "]...");

        Contact contact = contactRepository.findById(id);

        if (contact == null) {
            Log.w(logger, () -> "Contact with id [" + id + "] not found");
        } else {
            Log.d(logger, () -> "Found contact by id [" + id + "]: " + contact);
        }

        return contact;
    }

    @Override
    public Contact save(Contact contact) {
        Objects.requireNonNull(contact);

        Log.d(logger, () -> "Saving contact: " + contact);

        Contact result;

        if (isNew(contact)) {
            if (contact.getId() == null || contact.getId().isEmpty()) {
                contact.setId(UUID.randomUUID().toString());
            }

            result = contactRepository.insert(contact);
        } else {
            result = contactRepository.update(contact);
        }

        String id = result.getId();
        Log.d(logger, () -> "Contact was saved: id = " + id);

        return result;
    }

    @Override
    public boolean isNew(Contact contact) {
        Objects.requireNonNull(contact);

        return contact.getId() == null ||
                contact.getId().isEmpty() ||
                contactRepository.findById(contact.getId()) == null;
    }

    @Override
    public boolean delete(Contact contact) {
        if (contact == null ||
                contact.getId() == null ||
                contact.getId().isEmpty()) {
            return true;
        }
        return contactRepository.delete(contact);
    }
}
