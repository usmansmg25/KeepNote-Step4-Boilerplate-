package com.stackroute.keepnote.test.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.keepnote.config.ApplicationContextConfig;
import com.stackroute.keepnote.controller.UserController;
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@WebAppConfiguration
public class UserControllerTest {

	private MockMvc mockMvc;
	private User user;
	@Autowired
	private MockHttpSession session;
	@Mock
	private UserService userService;
	@InjectMocks
	private UserController userController = new UserController(userService);

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

		user = new User("Jhon123", "Jhon Simon", "123456", "9872367384", new Date());
		// Setting session attribute

		session.setAttribute("loggedInUserId", user.getUserId());
	}

	@Test
	public void testRegisterUserSuccess() throws Exception {
		when(userService.registerUser(any())).thenReturn(true);
		mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andExpect(status().isCreated()).andDo(print());
	}

	@Test
	public void testRegisterUserFailure() throws UserAlreadyExistException, Exception {

		when(userService.registerUser(any())).thenThrow(UserAlreadyExistException.class);
		mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andExpect(status().isConflict()).andDo(print());
	}

	@Test
	public void testUpdateUserSuccess() throws Exception {
		when(userService.updateUser(any(), eq(user.getUserId()))).thenReturn(user);

		user.setUserMobile("9898989898");
		mockMvc.perform(put("/user/{id}", user.getUserId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(user)).session(session)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateUserFailure() throws Exception {
		when(userService.updateUser(any(), eq(user.getUserId()))).thenReturn(null);

		user.setUserMobile("9898989898");
		mockMvc.perform(put("/user/{id}", user.getUserId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(user)).session(session)).andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateUserWithoutSessionFailure() throws Exception {
		when(userService.updateUser(any(), eq(user.getUserId()))).thenReturn(user);

		user.setUserMobile("9898989898");
		mockMvc.perform(
				put("/user/{id}", user.getUserId()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testDeleteUserSuccess() throws Exception {
		when(userService.deleteUser(user.getUserId())).thenReturn(true);
		mockMvc.perform(delete("/user/{id}", user.getUserId()).session(session)).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testDeleteUserFailure() throws Exception {
		when(userService.deleteUser(user.getUserId())).thenReturn(false);
		mockMvc.perform(delete("/user/{id}", user.getUserId()).session(session)).andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testDeleteUserWithoutSessionFailure() throws Exception {
		when(userService.deleteUser(user.getUserId())).thenReturn(false);
		mockMvc.perform(delete("/user/{id}", user.getUserId())).andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	public void testGetByUserIdSuccess() throws Exception {
		when(userService.getUserById(user.getUserId())).thenReturn(user);
		mockMvc.perform(get("/user/{id}", user.getUserId()).session(session)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testGetByUserIdFailure() throws Exception {
		when(userService.getUserById(user.getUserId())).thenReturn(null);
		mockMvc.perform(get("/user/{id}", user.getUserId()).session(session)).andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testGetByUserIdWithoutSessionFailure() throws Exception {
		when(userService.getUserById(user.getUserId())).thenReturn(null);
		mockMvc.perform(get("/user/{id}", user.getUserId())).andExpect(status().isUnauthorized()).andDo(print());
	}

	public static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
