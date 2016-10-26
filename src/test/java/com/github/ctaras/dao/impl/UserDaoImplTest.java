package com.github.ctaras.dao.impl;

import com.github.ctaras.dao.UserDao;
import com.github.ctaras.domain.User;
import com.github.ctaras.error.UserLoginAlreadyExistsException;
import com.github.ctaras.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoImplTest {

    private final String testId = "1c60344d-a403-4873-a141-ec4799731b03";
    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserDao userDao;

    @Before
    public void setUp() {
        userDao = new UserDaoImpl(userRepository, bCryptPasswordEncoder);
    }

    private User getTestUser() {
        User user = new User();
        user.setId(testId);
        user.setLogin("loginA");
        user.setPassword("123");
        user.setFullName("FUll Name");

        return user;
    }

    private List<User> getTestUserList() {
        List<User> users = new ArrayList<>();

        users.add(getTestUser());
        users.add(getTestUser());
        users.add(getTestUser());
        users.add(getTestUser());

        return users;
    }

    @Test
    public void findAll() {
        when(userRepository.findAll()).thenReturn(getTestUserList());

        List<User> result = userDao.findAll();
        verify(userRepository).findAll();

        assertThat(result, notNullValue());
        assertThat(result.size(), is(getTestUserList().size()));
    }

    @Test
    public void findById() {
        when(userRepository.findById(testId)).thenReturn(getTestUser());

        User user = userDao.findById(testId);
        verify(userRepository).findById(testId);
        assertThat(user, notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void findByIdException() {
        when(userRepository.findById(null)).thenReturn(null);
        userDao.findById(null);
    }

    @Test(expected = NullPointerException.class)
    public void saveNullException() throws UserLoginAlreadyExistsException {
        userDao.save(null);
    }

    @Test
    public void saveExistsUser() throws UserLoginAlreadyExistsException {
        User user = getTestUser();
        when(userRepository.findById(user.getId())).thenReturn(user);
        when(userRepository.update(user)).thenReturn(user);
        userDao.save(user);

        verify(userRepository).findById(user.getId());
        verify(userRepository).update(user);
    }

    @Test(expected = UserLoginAlreadyExistsException.class)
    public void saveNotExistsUser() throws UserLoginAlreadyExistsException {
        User user = getTestUser();
        when(userRepository.findById(user.getId())).thenReturn(null);
        when(userRepository.findByLogin(user.getLogin())).thenReturn(getTestUser());
        when(userRepository.insert(user)).thenReturn(user);
        userDao.save(user);

        verify(userRepository).findById(user.getId());
        verify(userRepository).insert(user);
    }

    @Test
    public void saveNotExistsUser_2() throws UserLoginAlreadyExistsException {
        User user = getTestUser();
        when(userRepository.findById(user.getId())).thenReturn(null);
        when(userRepository.findByLogin(user.getLogin())).thenReturn(null);
        when(userRepository.insert(user)).thenReturn(user);
        userDao.save(user);

        verify(userRepository).findById(user.getId());
        verify(userRepository).insert(user);
    }

    @Test(expected = NullPointerException.class)
    public void isNewNull() {
        userDao.isNew(null);
    }

    @Test
    public void isNew_1() {
        User user = new User();
        assertThat(userDao.isNew(user), is(true));
    }

    @Test
    public void isNew_2() {
        User user = new User();
        user.setId("");
        assertThat(userDao.isNew(user), is(true));
    }

    @Test
    public void isNew_3() {
        when(userRepository.findById(testId)).thenReturn(getTestUser());
        User user = new User();
        user.setId(testId);
        assertThat(userDao.isNew(user), is(false));
    }
}