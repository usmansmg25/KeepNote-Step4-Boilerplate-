package com.stackroute.keepnote.test.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.dao.ReminderDAO;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.service.NoteServiceImpl;

public class NoteServiceImplTest {

	@Mock
	private NoteDAO noteDAO;
	@Mock
	private CategoryDAO categoryDAO;
	@Mock
	private ReminderDAO reminderDAO;
	@InjectMocks
	NoteServiceImpl noteServiceImpl;

	private Note note;
	private Category category;
	private Reminder reminder;
	List<Note> notes = null;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		category = new Category(1, "Java", "Testing in java", new Date(), "Jhon123", null);
		reminder = new Reminder(1, "Email reminder", "daily reminder", "Active", "Jhon123", null, new Date());
		note = new Note(1, "Testing", "Testing Service layer", "Active", new Date(), category, reminder, "Jhon123");
		notes = new ArrayList<Note>();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateNoteSuccess() throws ReminderNotFoundException, CategoryNotFoundException {
		when(noteDAO.createNote(note)).thenReturn(true);
		boolean status = noteServiceImpl.createNote(note);
		assertEquals(true, status);
		verify(noteDAO, times(1)).createNote(note);
		verify(categoryDAO, times(1)).getCategoryById(category.getCategoryId());
		verify(reminderDAO, times(1)).getReminderById(reminder.getReminderId());

	}

	@Test
	public void testCreateNoteSuccessWithoutCategoryAndReminder()
			throws ReminderNotFoundException, CategoryNotFoundException {
		note = new Note(1, "Testing", "Testing Service layer", "Active", new Date(), null, null, "Jhon123");
		when(noteDAO.createNote(note)).thenReturn(true);
		boolean status = noteServiceImpl.createNote(note);
		assertEquals(true, status);
		verify(noteDAO, times(1)).createNote(note);

	}

	@Test
	public void testCreateNoteSuccessWithoutCategory() throws ReminderNotFoundException, CategoryNotFoundException {
		note = new Note(1, "Testing", "Testing Service layer", "Active", new Date(), null, reminder, "Jhon123");
		when(noteDAO.createNote(note)).thenReturn(true);
		boolean status = noteServiceImpl.createNote(note);
		assertEquals(true, status);
		verify(noteDAO, times(1)).createNote(note);
		verify(reminderDAO, times(1)).getReminderById(reminder.getReminderId());

	}

	@Test
	public void testCreateNoteSuccessWithoutReminder() throws ReminderNotFoundException, CategoryNotFoundException {
		note = new Note(1, "Testing", "Testing Service layer", "Active", new Date(), category, null, "Jhon123");
		when(noteDAO.createNote(note)).thenReturn(true);
		boolean status = noteServiceImpl.createNote(note);
		assertEquals(true, status);
		verify(noteDAO, times(1)).createNote(note);
		verify(categoryDAO, times(1)).getCategoryById(category.getCategoryId());

	}

	@Test()
	public void testCreateNoteFailure() throws ReminderNotFoundException, CategoryNotFoundException {
		when(noteDAO.createNote(note)).thenReturn(false);
		boolean status = noteServiceImpl.createNote(note);
		assertEquals(false, status);
		verify(noteDAO, times(1)).createNote(note);
		verify(categoryDAO, times(1)).getCategoryById(category.getCategoryId());
		verify(reminderDAO, times(1)).getReminderById(reminder.getReminderId());

	}

	@Test(expected = ReminderNotFoundException.class)
	public void testCreateNoteWithoutCategoryFailure() throws ReminderNotFoundException, CategoryNotFoundException {
		reminder = new Reminder(2, "Email reminder", "daily reminder", "Active", "Jhon123", null, new Date());
		note = new Note(1, "Testing", "Testing Service layer", "Active", new Date(), null, reminder, "Jhon123");
		when(reminderDAO.getReminderById(2)).thenThrow(ReminderNotFoundException.class);
		when(noteDAO.createNote(note)).thenReturn(true);
		@SuppressWarnings("unused")
		boolean status = noteServiceImpl.createNote(note);

	}

	@Test
	public void testDeleteNoteSuccess() throws NoteNotFoundException {
		when(noteDAO.deleteNote(1)).thenReturn(true);
		boolean status = noteServiceImpl.deleteNote(1);
		assertEquals(true, status);
		verify(noteDAO, times(1)).deleteNote(1);
	}

	@Test
	public void testDeleteNoteFailure() throws NoteNotFoundException {
		when(noteDAO.deleteNote(1)).thenReturn(false);
		boolean status = noteServiceImpl.deleteNote(1);
		assertEquals(false, status);
		verify(noteDAO, times(1)).deleteNote(1);
	}

	@Test
	public void testGetAllNotesByUserIdSucess() {

		notes.add(note);
		note = new Note(2, "Testing-2", "Testing Service layer", "Active", new Date(), category, reminder, "Jhon123");
		notes.add(note);
		note = new Note(3, "Testing-3", "Testing Service layer", "Active", new Date(), category, reminder, "Jhon123");
		notes.add(note);
		when(noteDAO.getAllNotesByUserId("Jhon123")).thenReturn(notes);
		List<Note> allNotes = noteServiceImpl.getAllNotesByUserId("Jhon123");
		assertEquals(3, allNotes.size());
		assertEquals(notes, allNotes);
		assertEquals("Testing-3", allNotes.get(2).getNoteTitle());
		verify(noteDAO, times(1)).getAllNotesByUserId("Jhon123");

	}

	@Test
	public void testGetNoteByIdSuccess() throws NoteNotFoundException {
		when(noteDAO.getNoteById(note.getNoteId())).thenReturn(note);
		Note fetchedNote = noteServiceImpl.getNoteById(note.getNoteId());
		assertEquals("Testing", fetchedNote.getNoteTitle());
		assertEquals(note, fetchedNote);
		verify(noteDAO, times(1)).getNoteById(note.getNoteId());
	}

	@Test(expected = NoteNotFoundException.class)
	public void testGetNoteByIdFailure() throws NoteNotFoundException {
		when(noteDAO.getNoteById(2)).thenThrow(NoteNotFoundException.class);
		@SuppressWarnings("unused")
		Note fetchedNote = noteServiceImpl.getNoteById(2);

	}

	@Test
	public void testUpdateNoteSuccess()
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {

		note.setNoteContent("Testing updateNote()");
		when(reminderDAO.getReminderById(1)).thenReturn(reminder);
		when(categoryDAO.getCategoryById(1)).thenReturn(category);
		when(noteDAO.getNoteById(1)).thenReturn(note);
		when(noteDAO.UpdateNote(note)).thenReturn(true);
		Note updatedNote = noteServiceImpl.updateNote(note, 1);
		assertEquals("Testing updateNote()", updatedNote.getNoteContent());
		verify(categoryDAO, times(1)).getCategoryById(category.getCategoryId());
		verify(noteDAO, times(1)).getNoteById(1);
		verify(noteDAO, times(1)).UpdateNote(note);
		verify(reminderDAO, times(1)).getReminderById(1);
	}

	@Test
	public void testUpdateNoteWithoutReminderSuccess()
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {

		note.setNoteContent("Testing updateNote()");
		note.setReminder(null);
		when(reminderDAO.getReminderById(1)).thenReturn(null);
		when(categoryDAO.getCategoryById(1)).thenReturn(category);
		when(noteDAO.getNoteById(1)).thenReturn(note);
		when(noteDAO.UpdateNote(note)).thenReturn(true);
		Note updatedNote = noteServiceImpl.updateNote(note, 1);
		assertEquals("Testing updateNote()", updatedNote.getNoteContent());
		verify(categoryDAO, times(1)).getCategoryById(category.getCategoryId());
		verify(noteDAO, times(1)).getNoteById(1);
		verify(noteDAO, times(1)).UpdateNote(note);

	}

	@Test
	public void testUpdateNoteWithoutCategorySuccess()
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {

		note.setNoteContent("Testing updateNote()");
		note.setCategory(null);
		when(reminderDAO.getReminderById(1)).thenReturn(reminder);
		when(categoryDAO.getCategoryById(1)).thenReturn(null);
		when(noteDAO.getNoteById(1)).thenReturn(note);
		when(noteDAO.UpdateNote(note)).thenReturn(true);
		Note updatedNote = noteServiceImpl.updateNote(note, 1);
		assertEquals("Testing updateNote()", updatedNote.getNoteContent());
		verify(noteDAO, times(1)).getNoteById(1);
		verify(noteDAO, times(1)).UpdateNote(note);
		verify(reminderDAO, times(1)).getReminderById(1);
	}

	@Test
	public void testUpdateNoteWithoutCategoryAndReminderSuccess()
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {

		note.setNoteContent("Testing updateNote()");
		note.setReminder(null);
		note.setCategory(null);
		when(reminderDAO.getReminderById(1)).thenReturn(null);
		when(categoryDAO.getCategoryById(1)).thenReturn(null);
		when(noteDAO.getNoteById(1)).thenReturn(note);
		when(noteDAO.UpdateNote(note)).thenReturn(true);
		Note updatedNote = noteServiceImpl.updateNote(note, 1);
		assertEquals("Testing updateNote()", updatedNote.getNoteContent());
		verify(noteDAO, times(1)).getNoteById(1);
		verify(noteDAO, times(1)).UpdateNote(note);

	}

	@Test(expected = NoteNotFoundException.class)
	public void testUpdateNoteFailure()
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {

		note.setNoteContent("Testing updateNote()");
		when(reminderDAO.getReminderById(1)).thenReturn(reminder);
		when(categoryDAO.getCategoryById(1)).thenReturn(category);
		when(noteDAO.getNoteById(1)).thenThrow(NoteNotFoundException.class);
		when(noteDAO.UpdateNote(note)).thenReturn(true);
		@SuppressWarnings("unused")
		Note updatedNote = noteServiceImpl.updateNote(note, 1);

	}

}
