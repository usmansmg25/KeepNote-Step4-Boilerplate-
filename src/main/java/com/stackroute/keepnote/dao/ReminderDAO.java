package com.stackroute.keepnote.dao;

import java.util.List;

import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;

public interface ReminderDAO {
	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Impl classes
	 */

	public boolean createReminder(Reminder reminder);

	public boolean updateReminder(Reminder reminder);

	public boolean deleteReminder(int reminderId);

	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException;

	public List<Reminder> getAllReminderByUserId(String userId);
}