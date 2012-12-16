package com.internal.recipes.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private User user;
	
	@Before
	public void setUp() {
		user = new User("cherb", "password");
	}
	
	@After
	public void tearDown() {
		user = null;
	}
	@Test
	public void testUser() {
		
		assertEquals("cherb", user.getUserName());
		assertEquals("password", user.getPassword());
		assertNotNull(user.getRoles());
		
		assertTrue(user.getRoles().contains(Role.ROLE_GUEST));
	}

	@Test
	public void testGetRoles() {
		user.getRoles().add(Role.ROLE_ADMINISTRATOR);
		assertTrue(user.getRoles().size() == 2);
		assertTrue(user.getRoles().contains(Role.ROLE_ADMINISTRATOR));
	}

}
