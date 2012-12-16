package com.internal.recipes.repository;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.internal.recipes.domain.User;

@RepositoryDefinition(domainClass = User.class, idClass = String.class)
public interface UserRepository {
	
	User save(User user);

	User findByUserName(String userName);

	List<User> findAll();

	Long count();

	void delete(User user);

	boolean exists(String id);
	
}
