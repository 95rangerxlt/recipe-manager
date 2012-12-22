package com.internal.recipes.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.internal.recipes.domain.User;

@RepositoryDefinition(domainClass = User.class, idClass = String.class)
public interface UserRepository {
	
	User save(User user);

	User findByUserName(String userName);

	User findOne(String primaryKey);

	List<User> findAll();

	// filter out the password field
	@Query(value = "{}", fields = "{'password': 0 }")
	List<User> getAll();
	
	@Query(value = "{}", fields = "{'userName': 1, 'firstName':1, 'lastName':1, 'emailAddress':1 }")	
	User getUserInfo(String userName);

	Long count();

	void delete(User user);

	boolean exists(String id);	
}
