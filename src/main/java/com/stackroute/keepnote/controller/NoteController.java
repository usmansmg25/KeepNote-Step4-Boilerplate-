package com.stackroute.keepnote.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.service.NoteService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class NoteController {

	/*
	 * Autowiring should be implemented for the NoteService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	private Log log = LogFactory.getLog(getClass());
	
	private NoteService noteService;

	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	/*
	 * Define a handler method which will create a specific note by reading the
	 * Serialized object from request body and save the note details in a Note table
	 * in the database.Handle ReminderNotFoundException and
	 * CategoryNotFoundException as well. please note that the loggedIn userID
	 * should be taken as the createdBy for the note.This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 201(CREATED) - If the note created successfully. 2. 409(CONFLICT) - If the
	 * noteId conflicts with any existing user3. 401(UNAUTHORIZED) - If the user
	 * trying to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/note" using HTTP POST method
	 */
	@PostMapping("/note")
	public ResponseEntity<?> createNote(@RequestBody Note note,HttpServletRequest request) {
		log.info("createNote : STARTED");
		HttpHeaders headers = new HttpHeaders();
		String loggedInUser =(String) request.getSession().getAttribute("loggedInUserId");
		if(loggedInUser== null)
		{
			return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
		}
		try {
			Reminder reminder = note.getReminder();
			if(reminder!=null)
			{
				log.info("reminder id"+reminder.getReminderId());
			}
			note.setNoteCreatedAt(new Date());
			note.setCreatedBy(loggedInUser);
			if(noteService.createNote(note))
			{
				return new ResponseEntity<>(headers, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
		}
		log.info("createNote : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
	}

	/*
	 * Define a handler method which will delete a note from a database handle NoteNotFoundException
	 * as well.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid noteId without {}
	 */
	@DeleteMapping("/note/{noteId}")
	public ResponseEntity<?> deleteNote(@PathVariable("noteId") int noteId, HttpServletRequest request) {
		log.info("deleteNote : ENDED");
		HttpHeaders headers = new HttpHeaders();
		String loggedInUser = (String) request.getSession().getAttribute("loggedInUserId");
		if (loggedInUser == null) {
			return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
		}
		try {
			if (noteService.deleteNote(noteId))
			{
				return new ResponseEntity<>(headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("deleteNote : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}

	/*
	 * Define a handler method which will update a specific note by reading the
	 * Serialized object from request body and save the updated note details in a
	 * note table in database handle ReminderNotFoundException,
	 * NoteNotFoundException, CategoryNotFoundException as well. please note that
	 * the loggedIn userID should be taken as the createdBy for the note. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note updated successfully. 2.
	 * 404(NOT FOUND) - If the note with specified noteId is not found. 3.
	 * 401(UNAUTHORIZED) - If the user trying to perform the action has not logged
	 * in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP PUT method.
	 */

	@PutMapping("/note/{noteId}")
	public ResponseEntity<?> updateNote(@RequestBody Note note, @PathVariable("noteId") int noteId,
			HttpServletRequest request) {
		log.info("updateNote : ENDED");
		HttpHeaders headers = new HttpHeaders();
		String loggedInUser = (String) request.getSession().getAttribute("loggedInUserId");
		if (loggedInUser == null) {
			return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
		}
		try {
			note.setNoteId(noteId);
			note.setCreatedBy(loggedInUser);
			note.setNoteCreatedAt(new Date());
			if(noteService.updateNote(note, noteId)!=null)
			{
				return new ResponseEntity<>(headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
		}
		log.info("updateNote : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}
	/*
	 * Define a handler method which will get us the notes by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/note" using HTTP GET method
	 */
	@GetMapping("/note")
	public ResponseEntity<?> getAllNotesByUserId(HttpServletRequest request) {
		log.info("getAllNotesByUserId : STARTED");
		HttpHeaders headers = new HttpHeaders();
		String loggedInUser =(String) request.getSession().getAttribute("loggedInUserId");
		if(loggedInUser== null)
		{
			return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
		}
		try {
				List<Note> notes = noteService.getAllNotesByUserId(loggedInUser);
				return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("getAllNotesByUserId : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
}