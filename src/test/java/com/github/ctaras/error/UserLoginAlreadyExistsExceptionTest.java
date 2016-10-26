package com.github.ctaras.error;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserLoginAlreadyExistsExceptionTest {

    @Test(expected = UserLoginAlreadyExistsException.class)
    public void testToThrowException() throws UserLoginAlreadyExistsException {
        throw new UserLoginAlreadyExistsException("loginA");
    }


    @Test
    public void testToContentException() {
        try {
            throw new UserLoginAlreadyExistsException("loginA");
        } catch (UserLoginAlreadyExistsException e) {
            assertEquals("loginA", e.login);
        }
    }

}