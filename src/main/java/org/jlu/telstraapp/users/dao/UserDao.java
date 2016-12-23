package org.jlu.telstraapp.users.dao;

import org.jlu.telstraapp.users.model.User;

public interface UserDao {
	User findByUserName(String username);
}