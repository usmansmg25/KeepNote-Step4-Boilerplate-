package com.stackroute.keepnote.test.controller;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.stackroute.keepnote.controller.ReminderController;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.ReminderService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@WebAppConfiguration
public class ReminderControllerTest {

	private MockMvc mockMvc;
	private Reminder reminder;
	private User user;
	@Autowired
	MockHttpSession session;
	@Mock
	ReminderService reminderService;
	@InjectMocks
	ReminderController reminderController;
	List<Reminder> allReminders = new ArrayList<Reminder>();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(reminderController).build();
		// Creating User object
		user = new User("Jhon123", "Jhon Simon", "974324567", "123456", new Date());
		// creating Session object and setting session
		session.setAttribute("loggedInUserId", user.getUserId());
		reminder = new Reminder(1, "Email", "Email reminder", "notification", "Jhon123", null, new Date());
	}

	@Test
	
	public void testCreateReminderSuccess() throws Exception {
		when(reminderService.createReminder(any())).thenReturn(true);
		mockMvc.perform(post("/reminder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(reminder))
				.session(session)).andExpect(status().isCreated());
	}

	@Test
	public void testCreateReminderFailure() throws Exception {
		when(reminderService.createReminder(any())).thenReturn(false);
		mockMvc.perform(post("/reminder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(reminder))
				.session(session)).andExpect(status().isConflict());
	}

	@Test
	public void testCreateReminderWithoutSessionFailure() throws Exception {
		when(reminderService.createReminder(any())).thenReturn(true);
		mockMvc.perform(post("/reminder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(reminder)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testDeleteReminderSuccess() throws Exception {
		when(reminderService.deleteReminder(reminder.getReminderId())).thenReturn(true);
		mockMvc.perform(delete("/reminder/{id}", reminder.getReminderId()).session(session)).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testDeleteReminderFailure() throws Exception {
		when(reminderService.deleteReminder(reminder.getReminderId())).thenReturn(false);
		mockMvc.perform(delete("/reminder/{id}", reminder.getReminderId()).session(session))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testDeleteReminderWithoutSessionFailure() throws Exception {
		when(reminderService.deleteReminder(reminder.getReminderId())).thenReturn(false);
		mockMvc.perform(delete("/reminder/{id}", reminder.getReminderId())).andExpect(status().isUnauthorized())
				.andDo(print());
	}

	@Test
	public void testUpdateReminderSuccess() throws Exception {
		when(reminderService.updateReminder(any(),eq( reminder.getReminderId()))).thenReturn(reminder);
		reminder.setReminderDescription("updating reminder");

		mockMvc.perform(put("/reminder/{id}", reminder.getReminderId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(reminder)).session(session)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateReminderFailure() throws Exception {
		when(reminderService.updateReminder(any(),eq( reminder.getReminderId()))).thenReturn(null);
		reminder.setReminderDescription("updating reminder");

		mockMvc.perform(put("/reminder/{id}", reminder.getReminderId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(reminder)).session(session)).andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateReminderWithoutSessionFailure() throws Exception {
		when(reminderService.updateReminder(any(),eq( reminder.getReminderId()))).thenReturn(null);
		reminder.setReminderDescription("updating reminder");

		mockMvc.perform(put("/reminder/{id}", reminder.getReminderId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(reminder))).andExpect(status().isUnauthorized());
	}

	@Test
	public void testGetAllRemindersByUserIdSuccess() throws Exception {
		allReminders.add(reminder);
		reminder = new Reminder(2, "Email-2", "Email reminder", "notification", "Jhon123", null, new Date());
		allReminders.add(reminder);
		reminder = new Reminder(2, "Email-2", "Email reminder", "notification", "Jhon123", null, new Date());
		allReminders.add(reminder);

		when(reminderService.getAllReminderByUserId(user.getUserId())).thenReturn(allReminders);
		mockMvc.perform(get("/reminder").contentType(MediaType.APPLICATION_JSON).session(session))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAllRemindersByUserIdWithoutSessionFailure() throws Exception {
		allReminders.add(reminder);
		reminder = new Reminder(2, "Email-2", "Email reminder", "notification", "Jhon123", null, new Date());
		allReminders.add(reminder);
		reminder = new Reminder(2, "Email-2", "Email reminder", "notification", "Jhon123", null, new Date());
		allReminders.add(reminder);

		when(reminderService.getAllReminderByUserId(user.getUserId())).thenReturn(allReminders);
		mockMvc.perform(get("/reminder").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	public void testGetReminderByIdSuccess() throws Exception {
		when(reminderService.getReminderById(reminder.getReminderId())).thenReturn(reminder);
		mockMvc.perform(get("/reminder/{id}", reminder.getReminderId()).contentType(MediaType.APPLICATION_JSON)
				.session(session)).andExpect(status().isOk());

	}

	@Test
	public void testGetReminderByIdFailure() throws Exception {
		when(reminderService.getReminderById(reminder.getReminderId())).thenReturn(null);
		mockMvc.perform(get("/reminder/{id}", reminder.getReminderId()).contentType(MediaType.APPLICATION_JSON)
				.session(session)).andExpect(status().isNotFound());

	}

	@Test
	public void testGetReminderByIdWithoutSessionFailure() throws Exception {
		when(reminderService.getReminderById(reminder.getReminderId())).thenReturn(null);
		mockMvc.perform(get("/reminder/{id}", reminder.getReminderId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());

	}

	public static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
