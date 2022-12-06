package com.stackroute.keepnote.dao;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

public interface UserDAO {

	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Imp classes
	 */

	public boolean registerUser(User user);

	public boolean updateUser(User user);

	public User getUserById(String UserId);

	public boolean validateUser(String userName, String password) throws UserNotFoundException;

	public boolean deleteUser(String UserId);
}