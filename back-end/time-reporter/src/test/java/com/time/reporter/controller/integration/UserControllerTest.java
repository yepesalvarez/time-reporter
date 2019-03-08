package com.time.reporter.controller.integration;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.reporter.TimeReporterApplication;
import com.time.reporter.controller.exceptions.ErrorMessage;
import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.domain.exceptions.UserAlreadyExistException;
import com.time.reporter.domain.exceptions.UserInvalidDataException;
import com.time.reporter.persistence.entity.RoleEntity;
import com.time.reporter.persistence.repository.RoleRepository;
import com.time.reporter.persistence.repository.UserRepository;
import com.time.reporter.timereporter.testdatabuilder.RoleEntityTestDataBuilder;
import com.time.reporter.timereporter.testdatabuilder.UserDtoTestDataBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest (classes = { TimeReporterApplication.class })
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private UserDto userDto;
	
	static final String URL_CRUD_USERS = "http://localhost:8080/api/users";
	static final String USER_NOT_VALID_PARAMETERS_MESSAGE = "Not valid user's parameters";
	static final String USER_ALREADY_EXIST_MESSAGE = "This user already exists in the system";

	@Before
	public void setUp() {
		
		RoleEntity roleEntity = new RoleEntityTestDataBuilder().build();
		roleRepository.save(roleEntity);
		userDto = new UserDtoTestDataBuilder().build();

	}

	@After
	public void tearDown() {
		userRepository.deleteAll();
	}

	@Test
	public void testSaveUserNullUserName() throws Exception {
		
		userDto.setUsername(null);
		
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertEquals(USER_NOT_VALID_PARAMETERS_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		
	}
	
	@Test
	public void testSaveUserEmptyUserName() throws Exception {
		
		userDto.setUsername("");
		
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertEquals(USER_NOT_VALID_PARAMETERS_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		
	}
	
	@Test
	public void testSaveUserTooLongUserName() throws Exception {
		
		userDto.setUsername("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor."
				+ " Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
				+ " Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. "
				+ "Nulla consequat massa quis enim. Donec");
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertEquals(USER_NOT_VALID_PARAMETERS_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		
	}
	
	
	@Test
	public void testSaveRepeatedUser() throws Exception {
		
		saveUser(userDto);
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(409, result.getStatus());
		assertEquals(USER_ALREADY_EXIST_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(UserAlreadyExistException.class);
		
	}
	
	@Test
	public void testSaveUserEnabled() throws Exception {
		MockHttpServletResponse result = saveUser(userDto);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertNotNull(userDtoResult);
		assertEquals(200, result.getStatus());
		assertTrue(userDtoResult.getId() == 1L);
		assertTrue(userDtoResult.isEnabled());
		assertNull(userDtoResult.getPassword());
		
	}
	
	@Test
	public void testSaveUserNotEnabled() throws Exception {
		userDto.setEnabled(false);
		MockHttpServletResponse result = saveUser(userDto);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertNotNull(userDtoResult);
		assertEquals(200, result.getStatus());
		assertTrue(userDtoResult.getId() == 1L);
		assertFalse(userDtoResult.isEnabled());
		assertNull(userDtoResult.getPassword());
		
	}

	@Test
	public void testGetUserById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllUsersIntIntStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveUserById() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}
	
	public MockHttpServletResponse saveUser(UserDto userDto) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_CRUD_USERS)
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(new JSONObject(userDto).toString());
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}

}
