package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;

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
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */

	@Autowired
	SessionFactory sessionFactory;

	public CategoryDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new category
	 */
	public boolean createCategory(Category category) {

		System.out.println("createCategory  START:");
		boolean saveFlag = false;
		getSession().save(category);
		saveFlag = true;
		System.out.println("save flag: " + saveFlag);
		System.out.println("createCategory  END:");
		return saveFlag;
	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {
		try {
			int noRecordDeleted = getSession().createQuery("delete from Category where categoryId =" + categoryId)
					.executeUpdate();
			if (noRecordDeleted > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) {
		try {
			getSession().saveOrUpdate(category);
			return true;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {

		List<Category> categoryList = getSession().createCriteria(Category.class)
				.add(Restrictions.eq("categoryId", categoryId)).list();

		if (categoryList != null && !categoryList.isEmpty()) {
			System.out.println("categoryList size::: " + categoryList.size());
			return (Category) categoryList.get(0);
		}
		System.out.println("categoryList ::: " + categoryList);
		throw new CategoryNotFoundException("Category not found.");
	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		List<Category> categoryList = getSession().createCriteria(Category.class)
				.add(Restrictions.eq("categoryCreatedBy", userId)).list();
		if (categoryList != null && !categoryList.isEmpty()) {
			return categoryList;
		}
		return null;
	}

}