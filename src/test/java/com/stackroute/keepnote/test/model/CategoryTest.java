package com.stackroute.keepnote.test.model;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import com.stackroute.keepnote.model.Category;

public class CategoryTest {

	private Category category;

	@Before
	public void setUp() throws Exception {
		category = new Category();
		category.setCategoryId(1);
		category.setCategoryName("Testing");
		category.setCategoryDescription("All about testing application");
		category.setCategoryCreatedBy("Jh0n123");
		category.setCategoryCreationDate(new Date());
		category.setNotes(null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void Beantest() {
		new BeanTester().testBean(Category.class);

	}

}
