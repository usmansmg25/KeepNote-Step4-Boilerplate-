package com.stackroute.keepnote.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.keepnote.controller.UserAuthenticationController;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

public class UserAuthenticationControllerTest {

	private MockMvc mockMvc;
	private User user;
	@Mock
	UserService userService;
	@Autowired
	private MockHttpSession session;
	@InjectMocks
	UserAuthenticationController authController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
		user = new User("Jhon123", "Jhon Simon", "974324567", "123456", new Date());
		// creatin Session object and setting session
		session = new MockHttpSession();
		session.setAttribute("loggedInUserId", user.getUserId());
	}

	@Test
	public void testLoginSuccess() throws Exception {

		when(userService.validateUser(user.getUserId(), user.getUserPassword())).thenReturn(true);
		when(userService.getUserById(user.getUserId())).thenReturn(user);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testLoginFailure() throws Exception {

		when(userService.validateUser(user.getUserId(), user.getUserPassword())).thenReturn(false);
		when(userService.getUserById(user.getUserId())).thenReturn(user);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andExpect(status().isUnauthorized()).andDo(print());

	}

	@Test
	public void testLogoutSuccess() throws Exception {

		when(userService.validateUser(user.getUserId(), user.getUserPassword())).thenReturn(true);
		when(userService.getUserById(user.getUserId())).thenReturn(user);

		mockMvc.perform(get("/logout").session(session)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testLogoutFailure() throws Exception {

		when(userService.validateUser(user.getUserId(), user.getUserPassword())).thenReturn(false);
		when(userService.getUserById(user.getUserId())).thenReturn(user);

		mockMvc.perform(get("/logout")).andExpect(status().isBadRequest()).andDo(print());

	}

	public static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
