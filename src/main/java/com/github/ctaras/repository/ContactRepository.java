package com.github.ctaras.repository;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;

import java.util.List;

public interface ContactRepository {
    List<Contact> findAllByUser(User user);

    Contact insert(Contact contact);

    Contact update(Contact contact);

    Contact findById(String id);

    boolean delete(Contact contact);
}
