package com.stackroute.keepnote.test.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.service.CategoryServiceImpl;

public class CategoryServiceImplTest {

	@Mock
	CategoryDAO categoryDAO;
	@InjectMocks
	CategoryServiceImpl categoryServiceImpl;
	private Category category = null;
	private List<Category> allCategory = null;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		category = new Category(1, "Testing", "All about testing spring application", new Date(), "Jhon123", null);
		allCategory = new ArrayList<Category>();
	}

	@Test
	public void testCreateCategorySuccess() {

		when(categoryDAO.createCategory(category)).thenReturn(true);
		boolean status = categoryServiceImpl.createCategory(category);
		assertEquals(true, status);
		verify(categoryDAO, times(1)).createCategory(category);
	}

	@Test
	public void testCreateCategoryFailure() {
		when(categoryDAO.createCategory(category)).thenReturn(false);
		boolean status = categoryServiceImpl.createCategory(category);
		assertEquals(false, status);
		verify(categoryDAO, times(1)).createCategory(category);
	}

	@Test
	public void testDeleteCategorySuccess() {
		when(categoryDAO.deleteCategory(1)).thenReturn(true);
		boolean status = categoryServiceImpl.deleteCategory(1);
		assertEquals(true, status);
		verify(categoryDAO, times(1)).deleteCategory(1);
	}

	@Test
	public void testDeleteCategoryFailure() {
		when(categoryDAO.deleteCategory(1)).thenReturn(false);
		boolean status = categoryServiceImpl.deleteCategory(1);
		assertEquals(false, status);
		verify(categoryDAO, times(1)).deleteCategory(1);
	}

	@Test
	public void testUpdateCategorySuccess() throws CategoryNotFoundException {
		when(categoryDAO.getCategoryById(1)).thenReturn(category);
		category.setCategoryName("Testing Spring app");
		when(categoryDAO.updateCategory(category)).thenReturn(true);
		Category updatedCategory = categoryServiceImpl.updateCategory(category, 1);
		assertEquals("Testing Spring app", updatedCategory.getCategoryName());
		verify(categoryDAO, times(1)).getCategoryById(1);
		verify(categoryDAO, times(1)).updateCategory(category);
	}

	@Test(expected = CategoryNotFoundException.class)
	public void testUpdateCategoryFailure() throws CategoryNotFoundException {
		when(categoryDAO.getCategoryById(1)).thenReturn(null);
		when(categoryDAO.updateCategory(category)).thenReturn(true);
		@SuppressWarnings("unused")
		Category updatedCategory = categoryServiceImpl.updateCategory(category, 1);

	}

	@Test
	public void testGetCategoryByIdSuccess() throws CategoryNotFoundException {

		when(categoryDAO.getCategoryById(1)).thenReturn(category);
		Category fetechedCategory = categoryServiceImpl.getCategoryById(1);
		assertEquals(category, fetechedCategory);
		verify(categoryDAO, times(1)).getCategoryById(1);
	}

	@Test(expected = CategoryNotFoundException.class)
	public void testGetCategoryByIdFailure() throws CategoryNotFoundException {

		when(categoryDAO.getCategoryById(2)).thenReturn(null);
		@SuppressWarnings("unused")
		Category fetechedCategory = categoryServiceImpl.getCategoryById(2);

	}

	@Test
	public void testGetAllCategoryByUserIdSuccess() {

		allCategory.add(category);
		category = new Category(2, "Testing-2", "All about testing spring application", new Date(), "Jhon123", null);
		allCategory.add(category);
		category = new Category(3, "Testing-3", "All about testing spring application", new Date(), "Jhon123", null);
		allCategory.add(category);

		when(categoryDAO.getAllCategoryByUserId("Jhon123")).thenReturn(allCategory);
		List<Category> categories = categoryServiceImpl.getAllCategoryByUserId("Jhon123");
		assertEquals(3, categories.size());
		verify(categoryDAO, times(1)).getAllCategoryByUserId("Jhon123");
	}

}
