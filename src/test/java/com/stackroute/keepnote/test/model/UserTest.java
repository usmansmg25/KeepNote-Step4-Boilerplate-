package com.stackroute.keepnote.test.model;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import com.stackroute.keepnote.model.User;

public class UserTest {

	private User user;

	@Before
	public void setUp() throws Exception {

		user = new User();
		user.setUserId("Jhon123");
		user.setUserName("Jhon Simon");
		user.setUserPassword("123456");
		user.setUserMobile("9898989898");
		user.setUserAddedDate(new Date());

	}

	@Test
	public void test() {
		new BeanTester().testBean(User.class);
	}

}
