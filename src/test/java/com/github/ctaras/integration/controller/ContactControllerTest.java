package com.github.ctaras.integration.controller;

import com.github.ctaras.dao.ContactDao;
import com.github.ctaras.dao.UserDao;
import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.dto.ContactList;
import com.github.ctaras.error.UserLoginAlreadyExistsException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.CookieManager;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactControllerTest {

    private final String username = "username";
    private final String password = "password";
    private final String plainCreds = username + ":" + password;

    @Autowired
    ObjectProvider<RestTemplateBuilder> builderProvider;

    @Autowired
    Environment environment;

    @Autowired
    UserDao userDao;

    @Autowired
    ContactDao contactDao;

    public TestRestTemplate testRestTemplate(
            ObjectProvider<RestTemplateBuilder> builderProvider,
            Environment environment) {

        RestTemplateBuilder builder = builderProvider.getIfAvailable();
        TestRestTemplate template = builder == null ? new TestRestTemplate()
                : new TestRestTemplate(builder.build());
        template.setUriTemplateHandler(new LocalHostUriTemplateHandler(environment));
        return template;
    }

    private TestRestTemplate restTemplate;
    private HttpEntity<String> request;
    private User user;
    private HttpHeaders headers;

    @Before
    public void setUp() {

        user = userDao.findByLogin(username);
        if(user == null) {
            user = new User();
            user.setLogin(username);
            user.setPassword(password);
            user.setFullName("User Name");
            try {
                userDao.save(user);
            } catch (UserLoginAlreadyExistsException e) {
            }
        }

        restTemplate = testRestTemplate(builderProvider, environment);

        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        request = new HttpEntity<>(headers);

        getXSRF();
    }

    public void getXSRF() {
        HttpEntity<String> responseEntity = restTemplate.exchange("/contacts", HttpMethod.GET, request, String.class);
        CookieManager cookieManager = new CookieManager();
        List<String> cookieHeader = responseEntity.getHeaders().get("Set-Cookie");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if (cookieHeader != null) {
            for (String cookie : cookieHeader) {
                String[] tokens = cookie.split("=");
                if (tokens[0].equals("XSRF-TOKEN")) {
                    String[] tokenValue = tokens[1].split(";");
                    headers.add("X-XSRF-TOKEN", tokenValue[0]);
                }
                if (tokens[0].equals("JSESSIONID"))
                    headers.add("Cookie", cookie);
            }
        }
    }

    @Test
    public void list() {
        ResponseEntity<ContactList> response = restTemplate.exchange("/contacts", HttpMethod.GET,
                request, ContactList.class);
        ContactList contactList = response.getBody();

        assertThat(contactList.getListModel().size(), is(contactDao.findAllByUser(user).size()));
    }

    @Test
    public void get() {
        Contact contact = new Contact();
        contact.setLastName("setLastName");
        contact.setUserId(user.getId());
        contact.setFirstName("setFirstName");
        contact.setMiddleName("setMiddleName");
        contact.setMobilePhoneNumber("+380(90) 789-4651");
        contact.setHomePhoneNumber("+380(91) 789-4651");

        contact = contactDao.save(contact);

        ResponseEntity<Contact> response = restTemplate.exchange("/contacts/" + contact.getId(),
                HttpMethod.GET, request, Contact.class);
        Contact result = response.getBody();

        assertThat(result, is(contact));
    }

}