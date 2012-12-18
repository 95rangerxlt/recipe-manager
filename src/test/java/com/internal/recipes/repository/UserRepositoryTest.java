package com.internal.recipes.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.internal.recipes.domain.Role;
import com.internal.recipes.domain.User;

public class UserRepositoryTest {

	private UserRepository userRepository;
	private MongoTemplate mt;

	@Before
	public void setUp() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"META-INF/spring/app-context.xml");
		
		userRepository = (UserRepository) applicationContext.getBean("userRepository");
		mt = applicationContext.getBean(MongoTemplate.class);
		
		assertNotNull(userRepository);
		assertNotNull(mt);
		
	}

	@After
	public void tearDown() throws Exception {
		//mt.dropCollection(User.class);
	}
	@Test
	public void test() {
		User user = new User("testUser", "testPassword");
		userRepository.save(user);
		assertTrue(userRepository.exists(user.getId()));
		userRepository.delete(user);
		assertNull(userRepository.findByUserName(user.getUserName()));
	}
	
	@Test
	public void createUsers() {
		User user = new User("mherb", new StandardPasswordEncoder().encode("havefun"));
		user.setFirstName("Michael");
		user.getRoles().add(Role.ROLE_ADMINISTRATOR);		
		userRepository.save(user);
		
	}

}
