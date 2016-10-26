package com.github.ctaras.integration.repository;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Util {
    public Contact getContact(User user) {
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setLastName("Last name");
        contact.setFirstName("First name");
        contact.setMiddleName("Middle name");
        contact.setMobilePhoneNumber("380991234567");
        contact.setHomePhoneNumber("380991234567");
        contact.setAddress("Address 1");
        contact.setEmail("mail@gmail.com");

        contact.setUserId(user.getId());

        return contact;
    }

    public int countRowsCSV(String table) throws IOException {
        Path filePath = Paths.get(table);
        List<String> fileContent = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));
        return fileContent.size() - 1;
    }

    public void fillDatabaseCSV(String output, String input) throws IOException {
        ClassPathResource resource = new ClassPathResource(output);
        InputStream inputStream = resource.getInputStream();
        String content = IOUtils.toString(inputStream, "UTF-8");
        Files.write(Paths.get(input), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }
}
