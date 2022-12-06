package com.stackroute.keepnote.controller;

import java.util.Date;

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
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	private Log log = LogFactory.getLog(getClass());
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in a User table
	 * in the database. This handler method should return any one of the status
	 * messages basis on different situations: 1. 201(CREATED) - If the user created
	 * successfully. 2. 409(CONFLICT) - If the userId conflicts with any existing
	 * user
	 * 
	 * Note: ------ This method can be called without being logged in as well as
	 * when a new user will use the app, he will register himself first before
	 * login.
	 * 
	 * This handler method should map to the URL "/user/register" using HTTP POST
	 * method
	 */
	@PostMapping(value="/user/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) 
	{
		log.info("registerUser : STARTED");
		HttpHeaders headers = new HttpHeaders();
		System.out.println("user id : "+user.getUserId());
		try {
			user.setUserAddedDate(new Date());
			userService.registerUser(user);
		} catch (UserAlreadyExistException e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
		}
		log.info("registerUser : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * user table in database handle exception as well. This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 200(OK) - If the user updated successfully. 2. 404(NOT FOUND) - If the user
	 * with specified userId is not found. 3. 401(UNAUTHORIZED) - If the user trying
	 * to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP PUT method.
	 */
	@PutMapping(value="/user/{userId}")
	public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable("userId") String userId,HttpServletRequest request) throws UserNotFoundException
	{
		log.info("updateUser : STARTED");
		HttpHeaders headers = new HttpHeaders();
		try {
			String loggedInUser =(String) request.getSession().getAttribute("loggedInUserId");
			if(loggedInUser== null)
			{
				return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
			}
			if(userService.updateUser(user, userId)!=null)
			{
			    return new ResponseEntity<>(headers, HttpStatus.OK);
			}
		} catch (UserAlreadyExistException e) {
			e.printStackTrace();
			 return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
		}
		log.info("updateUser : ENDED");
		    return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}
	/*
	 * Define a handler method which will delete a user from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */
	@DeleteMapping(value="/user/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId, HttpServletRequest request) throws UserNotFoundException
	{
		log.info("deleteUser : STARTED");
		HttpHeaders headers = new HttpHeaders();
		try {
				String loggedInUser =(String) request.getSession().getAttribute("loggedInUserId");
				if(loggedInUser== null)
				{
					return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
				}
				if(userService.deleteUser(userId))
				{
					return new ResponseEntity<>(headers, HttpStatus.OK);
				}
			
		}  catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
		}
		log.info("deleteUser : ENDED");
		    return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}
	/*
	 * Define a handler method which will show details of a specific user handle
	 * UserNotFoundException as well. This handler method should return any one of
	 * the status messages basis on different situations: 1. 200(OK) - If the user
	 * found successfully. 2. 401(UNAUTHORIZED) - If the user trying to perform the
	 * action has not logged in. 3. 404(NOT FOUND) - If the user with specified
	 * userId is not found. This handler method should map to the URL "/user/{id}"
	 * using HTTP GET method where "id" should be replaced by a valid userId without
	 * {}
	 */
	@GetMapping(value="/user/{userId}")
	public ResponseEntity<?> getUser(@PathVariable("userId") String userId,HttpServletRequest request) throws UserNotFoundException
	{
		log.info("getUser : STARTED");
		log.info("user id : "+userId);
		HttpHeaders headers = new HttpHeaders();
		try {
			String loggedInUser =(String) request.getSession().getAttribute("loggedInUserId");
			if(loggedInUser== null)
			{
				return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
			}
				if(userService.getUserById(userId)!=null)
				{
					return new ResponseEntity<>(headers, HttpStatus.OK);
				}
			    
		}  catch (UserNotFoundException e) {
			e.printStackTrace();
		    return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
		}
		log.info("getUser : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}
}