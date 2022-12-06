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
import com.stackroute.keepnote.dao.ReminderDAO;
import com.stackroute.keepnote.dao.ReminderDAOImpl;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;

@RunWith(SpringRunner.class)
@Transactional
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ReminderDAOImplTest {

	@Autowired
	private SessionFactory sessionFactory;
	private ReminderDAO reminderDAO;
	private Reminder reminder;

	@Before
	public void setUp() throws Exception {
		reminderDAO = new ReminderDAOImpl(sessionFactory);
		reminder = new Reminder(1, "Email", "Email reminder", "notification", "Jhon123", null, new Date());
	}

	@After
	public void tearDown() throws Exception {
		Query query = sessionFactory.getCurrentSession().createQuery("DELETE from Reminder");
		query.executeUpdate();
	}

	@Test
	@Rollback(true)
	public void testCreateReminderSuccess() throws ReminderNotFoundException {
		reminderDAO.createReminder(reminder);
		Reminder savedReminder = reminderDAO.getReminderById(reminder.getReminderId());
		assertEquals(reminder, savedReminder);
	}

	@Test(expected = ReminderNotFoundException.class)
	@Rollback(true)
	public void testCreateReminderFailure() throws ReminderNotFoundException {
		reminderDAO.createReminder(reminder);
		@SuppressWarnings("unused")
		Reminder savedReminder = reminderDAO.getReminderById(2);

	}

	@Test
	@Rollback(true)
	public void testUpdateReminderSuccess() throws ReminderNotFoundException {
		reminderDAO.createReminder(reminder);
		Reminder savedReminder = reminderDAO.getReminderById(reminder.getReminderId());
		savedReminder.setReminderDescription("email notification");
		boolean status = reminderDAO.updateReminder(savedReminder);
		assertEquals(true, status);
		savedReminder = reminderDAO.getReminderById(savedReminder.getReminderId());
		assertEquals("email notification", savedReminder.getReminderDescription());
	}

	@Test
	public void testDeleteReminderSuccess() throws ReminderNotFoundException {
		reminderDAO.createReminder(reminder);
		Reminder savedReminder = reminderDAO.getReminderById(reminder.getReminderId());
		boolean status = reminderDAO.deleteReminder(savedReminder.getReminderId());
		assertEquals(true, status);
	}

	@Test
	public void testDeleteReminderFailure() throws ReminderNotFoundException {
		reminderDAO.createReminder(reminder);
		@SuppressWarnings("unused")
		Reminder savedReminder = reminderDAO.getReminderById(reminder.getReminderId());
		boolean status = reminderDAO.deleteReminder(2);
		assertEquals(false, status);
	}

	@Test
	public void testGetReminderByIdSuccess() throws ReminderNotFoundException {

		reminderDAO.createReminder(reminder);
		Reminder savedReminder = reminderDAO.getReminderById(reminder.getReminderId());
		assertEquals(reminder, savedReminder);

	}

	@Test(expected = ReminderNotFoundException.class)
	public void testGetReminderByIdFailure() throws ReminderNotFoundException {

		reminderDAO.createReminder(reminder);
		Reminder savedReminder = reminderDAO.getReminderById(2);
		assertEquals(reminder, savedReminder);

	}

	@Test
	public void testGetAllReminderByUserId() {
		reminderDAO.createReminder(reminder);
		reminder = new Reminder(2, "Email", "Email reminder", "notification", "Jhon123", null, new Date());
		reminderDAO.createReminder(reminder);
		reminder = new Reminder(3, "Email", "Email reminder", "notification", "Jhon123", null, new Date());
		reminderDAO.createReminder(reminder);
		List<Reminder> allReminder = reminderDAO.getAllReminderByUserId("Jhon123");
		assertEquals(3, allReminder.size());
	}

}
