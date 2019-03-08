package com.time.reporter.controller.integration;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.reporter.TimeReporterApplication;
import com.time.reporter.controller.exceptions.ErrorMessage;
import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.domain.exceptions.UserInvalidDataException;
import com.time.reporter.persistence.entity.UserEntity;
import com.time.reporter.persistence.repository.RoleRepository;
import com.time.reporter.persistence.repository.UserRepository;
import com.time.reporter.timereporter.testdatabuilder.RoleEntityTestDataBuilder;
import com.time.reporter.timereporter.testdatabuilder.UserDtoTestDataBuilder;
import com.time.reporter.timereporter.testdatabuilder.UserEntityTestDataBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest (classes = { TimeReporterApplication.class })
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class LoginControllerTest {
		
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	static final String URL_LOGIN = "http://localhost:8080/login";
	static final String INVALID_USER_PARAMETERS = "Not valid user's parameters";
	static final String NON_EXISTENT_USER_MESSAGE = "User does not exist";
	static final String NON_EXISTENT_USER  = "user.does.not@exist.com";
	static final String INCORRECT_PASSW = "password.Incorrect";
	static final String INCORRECT_PASSW_MESSAGE = "Bad credentials";
	static final String NOT_ENABLED_USER = "User is disabled";

	private UserDto userDto;
	private UserEntity userEntity;
	
	@Before
	public void setUp() {
		userDto = new UserDtoTestDataBuilder().build();
		roleRepository.save(new RoleEntityTestDataBuilder().build());
		userEntity = userRepository.save(new UserEntityTestDataBuilder().withUsername(userDto.getUsername())
				.withPassword(passwordEncoder.encode(userDto.getPassword()))
				.build());	
	}

	@After
	public void tearDown() {
		userRepository.deleteAll();
	}
	
	@Test
	public void testSigninOk() throws Exception {
		MockHttpServletResponse result = login(userDto);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertEquals(200, result.getStatus());
		assertNotNull(userDtoResult.getToken());
		assertNull(userDtoResult.getPassword());
	}
	
	@Test
	public void testSigninEmptyUsername() throws Exception {
		userDto.setUsername("");
		
		MockHttpServletResponse result = login(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(400, result.getStatus());
		assertEquals(INVALID_USER_PARAMETERS, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
	}
	
	@Test
	public void testSigninNullUsername() throws Exception {
		userDto.setUsername(null);
		
		MockHttpServletResponse result = login(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(400, result.getStatus());
		assertEquals(INVALID_USER_PARAMETERS, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
	}
	
	@Test
	public void testSigninUserDoesNotExist() throws Exception {
		userDto.setUsername(NON_EXISTENT_USER);
		
		MockHttpServletResponse result = login(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(400, result.getStatus());
		assertEquals(NON_EXISTENT_USER_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(InternalAuthenticationServiceException.class);
		assertEquals(InternalAuthenticationServiceException.class.getSimpleName(), errorMessage.getError());
	}
	
	@Test
	public void testSigninEmptyPassword() throws Exception {
		userDto.setPassword("");
		
		MockHttpServletResponse result = login(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(400, result.getStatus());
		assertEquals(INVALID_USER_PARAMETERS, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
	}
	
	@Test
	public void testSigninNullPassword() throws Exception {
		userDto.setPassword(null);
		
		MockHttpServletResponse result = login(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(400, result.getStatus());
		assertEquals(INVALID_USER_PARAMETERS, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
	}

	@Test
	public void testSigninIncorrectPassword() throws Exception {
		userDto.setPassword(INCORRECT_PASSW);
		
		MockHttpServletResponse result = login(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(400, result.getStatus());
		assertEquals(INCORRECT_PASSW_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(BadCredentialsException.class);
		assertEquals(BadCredentialsException.class.getSimpleName(), errorMessage.getError());
	}
	
	@Test
	public void testSigninNotEnabledUser() throws Exception {
		userEntity.setEnabled(false);
		userEntity = userRepository.save(userEntity);
		
		MockHttpServletResponse result = login(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(403, result.getStatus());
		assertEquals(NOT_ENABLED_USER, errorMessage.getDescription());
		assertThatExceptionOfType(DisabledException.class);
		assertEquals(DisabledException.class.getSimpleName(), errorMessage.getError());
	}
	
	public MockHttpServletResponse login(UserDto userDto) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(new JSONObject(userDto).toString());
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}

}
