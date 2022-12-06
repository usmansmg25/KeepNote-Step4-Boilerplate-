package com.stackroute.keepnote.test.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.keepnote.config.ApplicationContextConfig;
import com.stackroute.keepnote.controller.CategoryController;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.CategoryService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@WebAppConfiguration
public class CategoryControllerTest {

	
	private MockMvc mockMvc;
	@Mock
	private User user;
	@Mock
	private Note note;
	@Mock
	private Category category;
	@Autowired
	private MockHttpSession session;
	@Mock
	CategoryService categoryService;
	@InjectMocks
	CategoryController categoryController;
	private List<Category> categories = new ArrayList<Category>();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

		// Creating User object
		user = new User("Jhon123", "Jhon Simon", "974324567", "123456", new Date());

		// creating Session object and setting session
		session.setAttribute("loggedInUserId", user.getUserId());

		// Creating Category Object
		category = new Category(1, "Testing", "All about testing spring application", new Date(), null, null);

	}

	@Test

	public void testCreateCategorySuccess() throws Exception {

		when(categoryService.createCategory(any())).thenReturn(true);
		mockMvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.session(session).content(asJsonString(category))).andExpect(status().isCreated()).andDo(print());
		verify(categoryService, times(1)).createCategory(Mockito.any(Category.class));
		verifyNoMoreInteractions(categoryService);

	}

	@Test
	public void testCreateCategoryFailure() throws Exception {

		when(categoryService.createCategory(category)).thenReturn(false);
		mockMvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON).content(asJsonString(category))
				.session(session)).andExpect(status().isConflict()).andDo(print());

	}

	@Test
	public void testCreateCategoryWithoutSessionFailure() throws Exception {

		when(categoryService.createCategory(category)).thenReturn(false);
		mockMvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON).content(asJsonString(category)))
				.andExpect(status().isUnauthorized()).andDo(print());

	}

	@Test
	public void testDeleteCategorySuccess() throws Exception {

		when(categoryService.deleteCategory(category.getCategoryId())).thenReturn(true);
		mockMvc.perform(delete("/category/{id}", category.getCategoryId()).contentType(MediaType.APPLICATION_JSON)
				.session(session)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testDeleteCategoryFailure() throws Exception {

		when(categoryService.deleteCategory(category.getCategoryId())).thenReturn(false);
		mockMvc.perform(delete("/category/{id}", category.getCategoryId()).contentType(MediaType.APPLICATION_JSON)
				.session(session)).andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testDeleteCategoryWithoutSessionFailure() throws Exception {

		when(categoryService.deleteCategory(category.getCategoryId())).thenReturn(false);
		mockMvc.perform(delete("/category/{id}", category.getCategoryId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());

	}

	@Test
	
	public void testUpdateCategorySuccess() throws Exception {
		when(categoryService.updateCategory(any(), eq(category.getCategoryId()))).thenReturn(category);
		mockMvc.perform(put("/category/{id}", category.getCategoryId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(category)).session(session)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateCategoryFailure() throws Exception {
		when(categoryService.updateCategory(any(), eq(category.getCategoryId()))).thenReturn(null);
		mockMvc.perform(put("/category/{id}", 2).contentType(MediaType.APPLICATION_JSON).content(asJsonString(category))
				.session(session)).andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateCategoryWithoutSessionFailure() throws Exception {
		when(categoryService.updateCategory(any(), eq(category.getCategoryId()))).thenReturn(category);
		mockMvc.perform(put("/category/{id}", category.getCategoryId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(category))).andExpect(status().isUnauthorized());
	}

	@Test
	public void testGetAllCategoriesByUserIdSuccess() throws Exception {

		categories.add(category);
		category = new Category(2, "Testing-2", "All about testing spring application", new Date(), null, null);
		categories.add(category);
		category = new Category(3, "Testing-3", "All about testing spring application", new Date(), null, null);
		categories.add(category);

		when(categoryService.getAllCategoryByUserId("Jhon123")).thenReturn(categories);
		mockMvc.perform(get("/category").contentType(MediaType.APPLICATION_JSON).session(session))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andDo(print());
	}

	@Test
	public void testGetAllCategoriesByUserIdWithoutSessionFailure() throws Exception {

		categories.add(category);
		category = new Category(2, "Testing-2", "All about testing spring application", new Date(), null, null);
		categories.add(category);
		category = new Category(3, "Testing-3", "All about testing spring application", new Date(), null, null);
		categories.add(category);

		when(categoryService.getAllCategoryByUserId("Jhon123")).thenReturn(categories);
		mockMvc.perform(get("/category").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized())
				.andDo(print());
	}

	public static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
