package com.stackroute.keepnote.test.dao;

import static org.junit.Assert.*;

import java.util.Date;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.stackroute.keepnote.config.ApplicationContextConfig;
import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.dao.UserDaoImpl;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

@RunWith(SpringRunner.class)
@Transactional
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class UserDAOImplTest {

	@Autowired
	private SessionFactory sessionFactory;
	private UserDAO userDAO;
	private User user;

	@Before
	public void setUp() throws Exception {

		userDAO = new UserDaoImpl(sessionFactory);
		user = new User("Jhon123", "Jhon Simon", "123456", "9872367384", new Date());
		Query query = sessionFactory.getCurrentSession().createQuery("DELETE from User");
		query.executeUpdate();
	}

	@After
	public void tearDown() throws Exception {

		Query query = sessionFactory.getCurrentSession().createQuery("DELETE from User");
		query.executeUpdate();
	}

	@Test
	@Rollback(true)
	public void testRegisterUserSuccess() {
		userDAO.registerUser(user);
		User fetchedUser = userDAO.getUserById("Jhon123");
		assertEquals(user, fetchedUser);
		userDAO.deleteUser(user.getUserId());
	}

	@Test
	@Rollback(true)
	public void testRegisterUserFailure() {
		userDAO.registerUser(user);
		User fetchedUser = userDAO.getUserById("Jhon123");
		assertNotEquals("George3706", fetchedUser.getUserId());
		userDAO.deleteUser(user.getUserId());
	}

	@Test
	public void testUpdateUserSuccess() {
		userDAO.registerUser(user);
		User fetchedUser = userDAO.getUserById("Jhon123");
		fetchedUser.setUserMobile("77777777");
		userDAO.updateUser(fetchedUser);
		assertEquals("77777777", fetchedUser.getUserMobile());
		userDAO.deleteUser(fetchedUser.getUserId());

	}

	@Test
	public void testGetUserByIdSuccess() {

		userDAO.registerUser(user);
		User fetchedUser = userDAO.getUserById("Jhon123");
		assertEquals(user, fetchedUser);
		userDAO.deleteUser(fetchedUser.getUserId());
	}

	@Test
	public void testGetUserByIdFailure() {

		userDAO.registerUser(user);
		User fetchedUser = userDAO.getUserById("Jhon123");
		assertNotEquals("George123", fetchedUser.getUserId());
		userDAO.deleteUser(fetchedUser.getUserId());
	}

	@Test
	public void testValidateUserSuccess() throws UserNotFoundException {

		userDAO.registerUser(user);
		boolean status = userDAO.validateUser("Jhon123", "123456");
		assertEquals(true, status);

	}

	@Test(expected = UserNotFoundException.class)
	public void testValidateUserFailure() throws UserNotFoundException {

		userDAO.registerUser(user);
		@SuppressWarnings("unused")
		boolean validatedUser = userDAO.validateUser("George3706", "123456");

	}

	@Test
	public void testDeleteUserSuccess() {

		userDAO.registerUser(user);
		boolean status = userDAO.deleteUser("Jhon123");
		assertEquals(true, status);
		userDAO.deleteUser(user.getUserId());
	}

	@Test
	public void testDeleteUserFailure() {
		userDAO.registerUser(user);
		boolean status = userDAO.deleteUser("George3706");
		assertEquals(false, status);
		userDAO.deleteUser(user.getUserId());
	}

}
