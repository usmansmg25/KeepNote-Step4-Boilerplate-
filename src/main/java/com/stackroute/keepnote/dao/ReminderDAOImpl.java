package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;


@Repository
@Transactional
public class ReminderDAOImpl implements ReminderDAO {
	
	
	@Autowired
	SessionFactory sessionFactory;

	public ReminderDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new reminder
	 */

	public boolean createReminder(Reminder reminder) {
		System.out.println("createReminder  START:");
		boolean saveFlag = false;
		getSession().save(reminder);
		saveFlag = true;
		System.out.println("save flag: " + saveFlag);
		System.out.println("createReminder  END:");
		return saveFlag;

	}
	
	/*
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) {
		try {
			getSession().saveOrUpdate(reminder);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/*
	 * Remove an existing reminder
	 */
	
	public boolean deleteReminder(int reminderId) {
		
		try {
			int noRecordDeleted = getSession().createQuery("delete from Reminder where reminderId ="+reminderId).executeUpdate();
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
	 * Retrieve details of a specific reminder
	 */
	
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {
		List<Reminder> reminderList = getSession().createCriteria(Reminder.class).add(Restrictions.idEq(reminderId)).list();

		if (reminderList != null && !reminderList.isEmpty()) {
			return (Reminder) reminderList.get(0);
		}
		else
		{
			throw new ReminderNotFoundException("Reminder not found.");
		}
	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	
	public List<Reminder> getAllReminderByUserId(String userId) {
		List<Reminder> reminderList = getSession().createCriteria(Reminder.class).add(Restrictions.eq("reminderCreatedBy", userId)).list();
		if (reminderList != null && !reminderList.isEmpty()) {
			return reminderList;
		}
		return null;

	}

}