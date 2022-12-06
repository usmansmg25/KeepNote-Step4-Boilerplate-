package com.stackroute.keepnote.test.controller;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import org.junit.After;
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
import com.stackroute.keepnote.controller.NoteController;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.NoteService;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@WebAppConfiguration
public class NoteControllerTest {

	private MockMvc mockMvc;

	private User user;
	@Mock
	private Note note;

	private Category category;

	private Reminder reminder;
	@Mock
	private NoteService noteService;
	@Autowired
	private MockHttpSession session;
	@InjectMocks
	private NoteController noteController;

	private List<Note> allNotesByUserId = new ArrayList<Note>();

	@Before

	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
		// session = new MockHttpSession();
		// Creating User Object
		user = new User("Jhon123", "Jhon Simon", "974324567", "123456", new Date());
		category = new Category();
		reminder = new Reminder();

		// Setting session attribute

		session.setAttribute("loggedInUserId", user.getUserId());

		// Creating Category object

		category.setCategoryId(1);
		category.setCategoryName("Testing Spring");
		category.setCategoryDescription("All about testing spring application");
		category.setCategoryCreatedBy("Jhon123");
		category.setCategoryCreationDate(new Date());

		// Creating Reminder Object

		reminder.setReminderId(1);
		reminder.setReminderName("Email Notification");
		reminder.setReminderType("Email reminder");
		reminder.setReminderDescription("Sending notification using Email");
		reminder.setReminderCreatedBy("Jhon123");
		reminder.setReminderCreationDate(new Date());

		// creating Note Object
		note = new Note(1, "Testing for Step-3", "Complete testing for step-3", "Active", new Date(), category,
				reminder, user.getUserId());

		allNotesByUserId.add(note);

		note = new Note(2, "Testing for Step-4", "Complete testing for step-4", "Active", new Date(), category,
				reminder, user.getUserId());
		allNotesByUserId.add(note);

		note = new Note(3, "Testing for Step-5", "Complete testing for step-5", "Active", new Date(), category,
				reminder, user.getUserId());
		allNotesByUserId.add(note);

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test

	public void testCreateNoteSuccess() throws Exception {

		when(noteService.createNote(any())).thenReturn(true);
		System.out.println(noteService.createNote(note));

		mockMvc.perform(
				post("/note").contentType(MediaType.APPLICATION_JSON).content(asJsonString(note)).session(session))
				.andExpect(status().isCreated()).andDo(print());

	}

	@Test
	public void testCreateNoteFailure() throws Exception {

		when(noteService.createNote(any())).thenReturn(false);

		mockMvc.perform(
				post("/note").contentType(MediaType.APPLICATION_JSON).content(asJsonString(note)).session(session))
				.andExpect(status().isConflict()).andDo(print());

	}

	@Test
	public void testCreateNoteFailureWithoutSession() throws Exception {

		when(noteService.createNote(any())).thenReturn(true);

		mockMvc.perform(post("/note").contentType(MediaType.APPLICATION_JSON).content(asJsonString(note)))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	public void testDeleteNoteSuccess() throws Exception {

		when(noteService.deleteNote(note.getNoteId())).thenReturn(true);
		mockMvc.perform(delete("/note/{id}", note.getNoteId()).session(session)).andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	public void testDeleteNoteFailure() throws Exception {

		when(noteService.deleteNote(1)).thenReturn(false);
		mockMvc.perform(delete("/note/{id}", 1).session(session)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testDeleteNoteFailureWithoutSession() throws Exception {

		when(noteService.deleteNote(1)).thenReturn(false);
		mockMvc.perform(delete("/note/{id}", 1)).andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test

	public void testUpdateNoteSuccess() throws Exception {
		note = new Note(1, "Testing for Step-3", "Complete testing for step-3", "Active", new Date(), category,
				reminder, user.getUserId());
		note.setNoteContent("updating note card");
		when(noteService.updateNote(any(), eq(1))).thenReturn(note);
		mockMvc.perform(put("/note/{id}", note.getNoteId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(note)).session(session)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testUpdateNoteFailure() throws ReminderNotFoundException, Exception {
		note.setNoteContent("updating note card");
		when(noteService.updateNote(note, note.getNoteId())).thenReturn(null);
		mockMvc.perform(put("/note/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(asJsonString(note))
				.session(session)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testUpdateNoteFailureWithoutSession() throws ReminderNotFoundException, Exception {
		// when(noteService.getNoteById(5)).thenReturn(null);
		note.setNoteContent("updating note card");
		when(noteService.updateNote(note, note.getNoteId())).thenReturn(note);
		mockMvc.perform(put("/note/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(asJsonString(note)))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	public void testGetAllNotesByUserIdSuccess() throws Exception {

		when(noteService.getAllNotesByUserId("Jhon123")).thenReturn(allNotesByUserId);
		mockMvc.perform(get("/note").contentType(MediaType.APPLICATION_JSON).session(session))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andDo(print());

	}

	@Test
	public void testGetAllNotesByUserIdFailureWithoutSession() throws Exception {

		when(noteService.getAllNotesByUserId("Jhon123")).thenReturn(allNotesByUserId);
		mockMvc.perform(get("/note").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized())
				.andDo(print());

	}

	public static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
