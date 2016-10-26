package com.github.ctaras.dao;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;

import java.util.List;

public interface ContactDao {
    List<Contact> findAllByUser(User user);

    Contact findById(String id);

    Contact save(Contact contact);

    boolean isNew(Contact contact);

    boolean delete(Contact contact);
}
