package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */

	@Autowired
	SessionFactory sessionFactory;

	public NoteDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new note
	 */
	
	public boolean createNote(Note note) {
		System.out.println("createNote  START:");
		boolean saveFlag = false;
		getSession().save(note);
		saveFlag = true;
		System.out.println("save flag: " + saveFlag);
		System.out.println("createNote  END:");
		return saveFlag;
	}

	/*
	 * Remove an existing note
	 */
	
	public boolean deleteNote(int noteId) {

		try {
		int noRecordDeleted = getSession().createQuery("delete from Note where noteId ="+noteId).executeUpdate();
			if(noRecordDeleted>0)
			{
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			return false;
	}

	/*
	 * Retrieve details of all notes by userId
	 */
	
	public List<Note> getAllNotesByUserId(String userId) {
		List<Note> noteList = getSession().createCriteria(Note.class).add(Restrictions.eq("createdBy", userId)).list();
		if (noteList != null && !noteList.isEmpty()) {
			return noteList;
		}
		return null;
	}

	/*
	 * Retrieve details of a specific note
	 */
	
	public Note getNoteById(int noteId) throws NoteNotFoundException {

		List<Note> noteList = getSession().createCriteria(Note.class).add(Restrictions.idEq(noteId)).list();

		if (noteList != null && !noteList.isEmpty()) {
			return (Note) noteList.get(0);
		} else {
			throw new NoteNotFoundException("Note not found.");
		}
	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) {
		try {
			getSession().saveOrUpdate(note);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}