package com.stackroute.keepnote.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

///* Annotate this class with @Aspect and @Component */

@Component
@Aspect
public class LoggingAspect {

	/*
	 * Write loggers for each of the methods of controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	
	private Log log = LogFactory.getLog(getClass());
	
	@Before("execution(* com.stackroute.keepnote.controller.NoteController.getAllNotesByUserId(..))")
	public void getAllCategoryByUserIdAdvice(){
		log.info("getAllCategoryByUserId before advice called...");
	}
	
	
	
    @After("execution(* com.stackroute.keepnote.controller.NoteController.*(..))")
    public void logAfterNote(JoinPoint point) {
        log.info(point.getSignature().getName() + " after called...");
       
    }
    
    @AfterReturning("execution(* com.stackroute.keepnote.controller.NoteController.*(..))")
    public void afterReturningNote(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterReturning called...");
       
    }
    
    @AfterThrowing("execution(* com.stackroute.keepnote.controller.NoteController.*(..))")
    public void afterThrowingNote(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterThrowing called...");
      
    }
    
    //----------------------------------------
    
    @Before("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
    public void logBeforeCategory(JoinPoint point) {
        log.info(point.getSignature().getName() + " before called...");
      
    }
    
    @After("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
    public void logAfterCategory(JoinPoint point) {
        log.info(point.getSignature().getName() + " after called...");
       
    }
    
    @AfterReturning("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
    public void afterReturningCategory(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterReturning called...");
       
    }
    
    @AfterThrowing("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
    public void afterThrowingCategory(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterThrowing called...");
      
    }
    
    //----------------------------------------------------
    
    @Before("execution(* com.stackroute.keepnote.controller.ReminderController.*(..))")
    public void logBeforeReminder(JoinPoint point) {
        log.info(point.getSignature().getName() + " before called...");
      
    }
    
    @After("execution(* com.stackroute.keepnote.controller.ReminderController.*(..))")
    public void logAfterReminder(JoinPoint point) {
        log.info(point.getSignature().getName() + " after called...");
       
    }
    
    @AfterReturning("execution(* com.stackroute.keepnote.controller.ReminderController.*(..))")
    public void afterReturningReminder(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterReturning called...");
       
    }
    
    @AfterThrowing("execution(* com.stackroute.keepnote.controller.ReminderController.*(..))")
    public void afterThrowingReminder(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterThrowing called...");
      
    }
    
    //--------------------------------------------
    
    @Before("execution(* com.stackroute.keepnote.controller.UserAuthenticationController.*(..))")
    public void logBeforeUserAuthentication(JoinPoint point) {
        log.info(point.getSignature().getName() + " before called...");
      
    }
    
    @After("execution(* com.stackroute.keepnote.controller.UserAuthenticationController.*(..))")
    public void logAfterUserAuthentication(JoinPoint point) {
        log.info(point.getSignature().getName() + " after called...");
       
    }
    
    @AfterReturning("execution(* com.stackroute.keepnote.controller.UserAuthenticationController.*(..))")
    public void afterReturningUserAuthentication(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterReturning called...");
       
    }
    
    @AfterThrowing("execution(* com.stackroute.keepnote.controller.UserAuthenticationController.*(..))")
    public void afterThrowingUserAuthentication(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterThrowing called...");
      
    }
    
    //----------------------------------------------------
    
    @Before("execution(* com.stackroute.keepnote.controller.UserController.*(..))")
    public void logBeforeUser(JoinPoint point) {
        log.info(point.getSignature().getName() + " before called...");
      
    }
    
    @After("execution(* com.stackroute.keepnote.controller.UserController.*(..))")
    public void logAfterUser(JoinPoint point) {
        log.info(point.getSignature().getName() + " after called...");
       
    }
    
    @AfterReturning("execution(* com.stackroute.keepnote.controller.UserController.*(..))")
    public void afterReturningUser(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterReturning called...");
       
    }
    
    @AfterThrowing("execution(* com.stackroute.keepnote.controller.UserController.*(..))")
    public void afterThrowingUser(JoinPoint point) {
        log.info(point.getSignature().getName() + " afterThrowing called...");
      
    }
}