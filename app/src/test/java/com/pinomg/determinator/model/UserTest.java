package com.pinomg.determinator.model;

import junit.framework.TestCase;

/**
 * Created by olle on 25/05/15.
 */
public class UserTest extends TestCase {

    private final String DEFAULT_USERNAME = "user";

    private User user;

    public void setUp() throws Exception {
        super.setUp();

        user = new User(DEFAULT_USERNAME);
    }

    /**
     * Test getting a user's username
     */
    public void testGetUsername() {
        assertEquals(user.getUsername(), DEFAULT_USERNAME);
    }

    /**
     * Test of equals implementation to be reflexive, symmetric, and transitive.
     */
    public void testEquals() {

        User compare = null;
        assertFalse(user.equals(compare));

        compare = new User("other_user");
        assertFalse(user.equals(compare));

        compare = new UserSubClass(DEFAULT_USERNAME);
        assertFalse(user.equals(compare));

        compare = new User(DEFAULT_USERNAME);
        assertTrue(user.equals(compare) && compare.equals(user));
        assertTrue(user.hashCode() == compare.hashCode());

    }

    /**
     * Sub class to User, for testing of equals
     */
    private class UserSubClass extends User {
        public UserSubClass(String username) {
            super(username);
        }
    }
}