package com.stackroute.keepnote.test.dao;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.List;

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
import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.dao.NoteDAOImpl;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;

@RunWith(SpringRunner.class)
@Transactional
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class NoteDAOImplTest {

	@Autowired
	private SessionFactory sessionFactory;
	private NoteDAO noteDAO;
	private Note note;

	@Before
	public void setUp() {
		noteDAO = new NoteDAOImpl(sessionFactory);
		note = new Note(1, "Testing-1", "Testing Service layer", "Active", new Date(), null, null, "Jhon123");

	}

	@After
	public void tearDown() throws Exception {
		Query query = sessionFactory.getCurrentSession().createQuery("DELETE from Note");
		query.executeUpdate();
	}

	@Test
	@Rollback(true)
	public void testCreateNoteSuccess() throws NoteNotFoundException {

		noteDAO.createNote(note);
		List<Note> notes = noteDAO.getAllNotesByUserId("Jhon123");
		assertEquals("Testing-1", notes.get(0).getNoteTitle());
		noteDAO.deleteNote(note.getNoteId());
	}

	@Test
	@Rollback(true)
	public void testCreateNoteFailure() throws NoteNotFoundException {

		noteDAO.createNote(note);
		List<Note> notes = noteDAO.getAllNotesByUserId("Jhon123");
		assertNotEquals("Testing-2", notes.get(0).getNoteTitle());
		noteDAO.deleteNote(note.getNoteId());

	}

	@Test
	@Rollback(true)
	public void testDeleteNoteSuccess() throws NoteNotFoundException {

		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(note.getNoteId());
		boolean status = noteDAO.deleteNote(noteData.getNoteId());
		assertEquals(true, status);

	}

	@Test
	public void testGetAllNotesByUserId() throws NoteNotFoundException {
		Note note2 = new Note(2, "Testing-2", "Testing Service layer", "Active", new Date(), null, null, "Jhon123");
		Note note3 = new Note(3, "Testing-3", "Testing Service layer", "Active", new Date(), null, null, "Jhon123");
		noteDAO.createNote(note);
		noteDAO.createNote(note2);
		noteDAO.createNote(note3);
		List<Note> notes = noteDAO.getAllNotesByUserId("Jhon123");
		assertEquals(3, notes.size());
		noteDAO.deleteNote(note.getNoteId());
		noteDAO.deleteNote(note2.getNoteId());
		noteDAO.deleteNote(note3.getNoteId());
	}

	@Test
	@Rollback(true)
	public void testGetNoteById() throws NoteNotFoundException {

		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(note.getNoteId());
		assertEquals(note, noteData);
		noteDAO.deleteNote(note.getNoteId());

	}

	@Test(expected = NoteNotFoundException.class)
	@Rollback(true)
	public void testGetNoteByIdFailure() throws NoteNotFoundException {

		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(2);
		assertEquals(note, noteData);
		noteDAO.deleteNote(note.getNoteId());

	}

	@Test
	@Rollback(true)
	public void testUpdateNote() throws NoteNotFoundException {
		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(note.getNoteId());
		noteData.setNoteContent("Unit testing for DAO layer");
		noteData.setNoteCreatedAt(new Date());
		;
		boolean status = noteDAO.UpdateNote(noteData);
		Note updatedNote = noteDAO.getNoteById(noteData.getNoteId());
		assertEquals("Unit testing for DAO layer", updatedNote.getNoteContent());
		assertEquals(true, status);
		noteDAO.deleteNote(updatedNote.getNoteId());

	}

}
