package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */

	@Autowired
	SessionFactory sessionFactory;

	public UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new user
	 */

	public boolean registerUser(User user) {
		System.out.println("registerUser  START:");
		try {
			getSession().save(user);
			return true;
		} catch (Exception e) {
			//throw new UserAlreadyExistException("User already exists");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Update an existing user
	 */

	public boolean updateUser(User user) {

		try {
			getSession().saveOrUpdate(user);
			return true;
		} catch (Exception e) {
		}
		return false;

	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String userId) {

		List<User> userList = getSession().createCriteria(User.class).add(Restrictions.eq("userId",userId)).list();

		if (userList != null && !userList.isEmpty()) {
			return (User) userList.get(0);
		}
		return null;
	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		
		/*List<User> userList = 	getSession().createQuery(" from User where userId ='"+userId+"' "
					+ "	and userPassword='"+password+"'").list();*/
		List<User> userList = getSession().
		createCriteria(User.class).
		add(Restrictions.eq("userId", userId)).
		add(Restrictions.eq("userPassword", password)).list();
		System.out.println("userList:  "+userList);
		if(userList!=null && !userList.isEmpty())
		{
			return true;
		}
			System.out.println("User not available: ");
			throw new UserNotFoundException("User not found");
	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) {
		try {
		int noRecordDeleted = getSession().createQuery("delete from User where userId ='"+userId+"'").executeUpdate();
			if(noRecordDeleted>0)
			{
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			return false;
	}

}