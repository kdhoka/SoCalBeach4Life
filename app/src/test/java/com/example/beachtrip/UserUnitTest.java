package com.example.beachtrip;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class UserUnitTest {
    @Test
    public void UserNullConstructorTest(){
        User user = new User();
        assertEquals(null, user.getEmail());
        assertEquals(null, user.getName());
        assertEquals(null, user.getPassword());
    }

    @Test
    public void UserConstructorTest(){
        User user = new User("name", "email", "password");
        assertEquals("name", user.getName());
        assertEquals("email", user.getEmail());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void UserGettersTest(){
        User user = new User("setName", "setEmail", "setPassword");

        assertEquals("setName", user.getName());
        assertEquals("setEmail", user.getEmail());
        assertEquals("setPassword", user.getPassword());
    }
}
