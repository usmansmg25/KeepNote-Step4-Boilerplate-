package com.stackroute.keepnote.test.model;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import com.stackroute.keepnote.model.Reminder;

public class ReminderTest {

	private Reminder reminder;

	@Before
	public void setUp() throws Exception {
		reminder = new Reminder();
		reminder.setReminderId(1);
		reminder.setReminderName("Email");
		reminder.setReminderDescription("Email Notification");
		reminder.setReminderType("email");
		reminder.setReminderCreatedBy("Jhon123");
		reminder.setReminderCreationDate(new Date());
		reminder.setNotes(null);
	}

	@Test
	public void Beantest() {

		new BeanTester().testBean(Reminder.class);

	}

}
