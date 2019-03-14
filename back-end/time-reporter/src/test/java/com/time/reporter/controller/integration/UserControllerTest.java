package com.time.reporter.controller.integration;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.AccessDeniedException;
import java.util.List;

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
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.reporter.TimeReporterApplication;
import com.time.reporter.controller.exceptions.ErrorMessage;
import com.time.reporter.controller.exceptions.ParametersMissmatchException;
import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.domain.enums.Roles;
import com.time.reporter.domain.exceptions.PasswordNotAllowedException;
import com.time.reporter.domain.exceptions.RoleDoesNotExistException;
import com.time.reporter.domain.exceptions.UserAlreadyExistException;
import com.time.reporter.domain.exceptions.UserInvalidDataException;
import com.time.reporter.persistence.entity.RoleEntity;
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
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private UserDto userDto;
	private RoleEntity adminRoleEntity;
	private UserEntity userEntity;
	
	static final String URL_CRUD_USERS = "http://localhost:8080/api/users";
	static final String URL_LOGIN = "http://localhost:8080/login";
	static final String USER_NOT_VALID_PARAMETERS_MESSAGE = "Not valid user's parameters";
	static final String USER_ALREADY_EXIST_MESSAGE = "This user already exists in the system";
	static final String USER_PASSW_NOT_VALID_MESSAGE = "Password does not fulfill the security policy";
	static final String USER_PASSW_NOT_VALID = "abc123";
	static final String NOT_VALID_JSON_PARAMETERS_MESSAGE = "Request's parameters do not match the expected types or values";
	static final String NOT_VALID_ROLE_MESSAGE = "Role does not exist";
	static final String ACCESS_DENIED_MESSAGE = "Access is denied";
	static final String UNAUTHORIZED_MESSAGE = "Full authentication is required to access this resource";
	static final String HEADER_AUTHORIZATION = "Authorization";
	static final String JSON_WEB_TOKEN_PREFIX = "Bearer ";
	static final String USER_NOT_ENABLED ="User is disabled";
	static final String ADMIN_ROLE_NAME = "ADMIN";
	
	@Before
	public void setUp() {
		
		RoleEntity userRoleEntity = new RoleEntityTestDataBuilder().build();
		roleRepository.save(userRoleEntity);
		adminRoleEntity = new RoleEntityTestDataBuilder()
				.withId(2L)
				.withName(ADMIN_ROLE_NAME)
				.withDescription("ADMIN ROLE")
				.build();
		adminRoleEntity = roleRepository.save(adminRoleEntity);
		userDto = new UserDtoTestDataBuilder().build();
		userEntity = new UserEntityTestDataBuilder().build();

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
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
		
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
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
		
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
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
		
	}
	
	@Test
	public void testSaveRepeatedUser() throws Exception {
		
		userRepository.save(userEntity);
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(409, result.getStatus());
		assertEquals(USER_ALREADY_EXIST_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(UserAlreadyExistException.class);
		assertEquals(UserAlreadyExistException.class.getSimpleName(), errorMessage.getError());
		
	}
	
	@Test
	public void testSaveUserNullPassword() throws Exception {
		
		userDto.setPassword(null);
		
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertEquals(USER_NOT_VALID_PARAMETERS_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(UserInvalidDataException.class);
		assertEquals(UserInvalidDataException.class.getSimpleName(), errorMessage.getError());
		
	}
	
	@Test
	public void testSaveUserEmptyPassword() throws Exception {
		
		userDto.setPassword("");
		
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertEquals(USER_PASSW_NOT_VALID_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(PasswordNotAllowedException.class);
		assertEquals(PasswordNotAllowedException.class.getSimpleName(), errorMessage.getError());
		
	}
	
	@Test
	public void testSaveUserNotSecurePassword() throws Exception {
		
		userDto.setPassword(USER_PASSW_NOT_VALID);
		
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertEquals(USER_PASSW_NOT_VALID_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(PasswordNotAllowedException.class);
		assertEquals(PasswordNotAllowedException.class.getSimpleName(), errorMessage.getError());
		
	}
	
	@Test
	public void testSaveUserEnabled() throws Exception {
		
		MockHttpServletResponse result = saveUser(userDto);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertNotNull(userDtoResult);
		assertEquals(200, result.getStatus());
		assertNotNull(userDtoResult.getId());
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
		assertNotNull(userDtoResult.getId());
		assertFalse(userDtoResult.isEnabled());
		
	}
	
	@Test
	public void testSaveUserNullEnabled() throws Exception {
		
		JSONObject jsonObj = new JSONObject("{\"username\":\"lorem.ipsum\",\"password\":\"*L0r3m1p5Un*\",\"role\":\"user\"}");
		
		MockHttpServletResponse result = saveUser(jsonObj);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertNotNull(userDtoResult);
		assertEquals(200, result.getStatus());
		assertNotNull(userDtoResult.getId());
		assertFalse(userDtoResult.isEnabled());
			
	}
	
	@Test
	public void testSaveUserObjectForEnabled() throws Exception {
		
		JSONObject jsonObj = new JSONObject("{\"username\":\"lorem.ipsum\",\"password\":\"*L0r3m1p5Un*\","
				+ "\"role\": \"user\",\"enabled\":{\"objectKey\":\"objectValue\"}}");
		
		MockHttpServletResponse result = saveUser(jsonObj);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertThatExceptionOfType(ParametersMissmatchException.class);
		assertEquals(ParametersMissmatchException.class.getSimpleName(), errorMessage.getError());
		assertEquals(NOT_VALID_JSON_PARAMETERS_MESSAGE, errorMessage.getDescription());
		
	}
	
	@Test
	public void testSaveUserStringForEnabled() throws Exception {
		
		JSONObject jsonObj = new JSONObject("{\"username\":\"lorem.ipsum\",\"password\":\"*L0r3m1p5Un*\","
				+ "\"role\": \"user\",\"enabled\":\"string\"}");
		
		MockHttpServletResponse result = saveUser(jsonObj);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertThatExceptionOfType(ParametersMissmatchException.class);
		assertEquals(ParametersMissmatchException.class.getSimpleName(), errorMessage.getError());
		assertEquals(NOT_VALID_JSON_PARAMETERS_MESSAGE, errorMessage.getDescription());
		
	}	
	
	@Test
	public void testSaveUserZeroForEnabled() throws Exception {
		
		JSONObject jsonObj = new JSONObject("{\"username\":\"lorem.ipsum\",\"password\":\"*L0r3m1p5Un*\",\"role\": \"user\",\"enabled\":0}");
		
		MockHttpServletResponse result = saveUser(jsonObj);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertNotNull(userDtoResult);
		assertEquals(200, result.getStatus());
		assertNotNull(userDtoResult.getId());
		assertFalse(userDtoResult.isEnabled());
		
	}
	
	@Test
	public void testSaveUserIntegerForEnabled() throws Exception {
		
		JSONObject jsonObj = new JSONObject("{\"username\":\"lorem.ipsum\",\"password\":\"*L0r3m1p5Un*\",\"role\": \"user\",\"enabled\":-25}");
		
		MockHttpServletResponse result = saveUser(jsonObj);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertNotNull(userDtoResult);
		assertEquals(200, result.getStatus());
		assertNotNull(userDtoResult.getId());
		assertTrue(userDtoResult.isEnabled());
		
	}
	
	@Test
	public void testSaveUserNullRole() throws Exception {
		
		userDto.setRole(null);
		
		MockHttpServletResponse result = saveUser(userDto);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertNotNull(userDtoResult);
		assertEquals(200, result.getStatus());
		assertNotNull(userDtoResult.getId());
		assertEquals(Roles.USER.toString(), userDtoResult.getRole());
		
	}
	
	@Test
	public void testSaveUserNonExistentRole() throws Exception {
		
		userDto.setRole("Non-existent-role");
		
		MockHttpServletResponse result = saveUser(userDto);
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(400, result.getStatus());
		assertEquals(NOT_VALID_ROLE_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(RoleDoesNotExistException.class);
		assertEquals(RoleDoesNotExistException.class.getSimpleName(), errorMessage.getError());
		
	}
	
	@Test
	public void testGetUserByIdOk() throws Exception {
		
		saveUser(userDto);	
		UserDto userDtoLogged = new ObjectMapper().readValue(doLogin(userDto).getContentAsString(), UserDto.class);
		Long idUserDtoLogged = userDtoLogged.getId();
		
		MockHttpServletResponse result = getUser(userDtoLogged.getToken(), idUserDtoLogged);
		UserDto userDtoResult = new ObjectMapper().readValue(result.getContentAsString(), UserDto.class);
		
		assertEquals(200, result.getStatus());
		assertNotNull(userDtoResult);
		assertEquals(idUserDtoLogged, userDtoResult.getId());

	}
	
	@Test
	public void testGetUserWithoutAuthorizationToken() throws Exception {
		
		userEntity = userRepository.save(userEntity);	
		
		MockHttpServletResponse result = getUser(null, userEntity.getId());
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(401, result.getStatus());
		assertNotNull(errorMessage);
		assertEquals(UNAUTHORIZED_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(InsufficientAuthenticationException.class);
		
	}


	@Test
	public void testGetAllUsersOk() throws Exception {
		
		userDto.setRole(ADMIN_ROLE_NAME);
		saveUser(userDto);
		
		MockHttpServletResponse result = getUsers(
				new ObjectMapper().readValue(doLogin(userDto).getContentAsString(), UserDto.class)
				.getToken());
		List<UserDto> usersDto = new ObjectMapper().readValue(result.getContentAsString(), new TypeReference<List<UserDto>>() { });
		
		assertEquals(200, result.getStatus());
		assertFalse(usersDto.isEmpty());
		assertNotNull(usersDto.get(0).getId());
	}
	
	@Test
	public void testGetAllUsersWithNotEnabledAdmin() throws Exception {
		
		saveUser(userDto);
		String userPassword = userDto.getPassword();
		MockHttpServletResponse loginResult = doLogin(userDto);
		userDto = new ObjectMapper().readValue(loginResult.getContentAsString(), UserDto.class);
		userEntity = new UserEntityTestDataBuilder()
				.withEnabled(false)
				.withId(userDto.getId())
				.withUsername(userDto.getUsername())
				.withPassword(userPassword)
				.withRoleEntity(adminRoleEntity)
				.build();
		userRepository.save(userEntity);
		
		MockHttpServletResponse result = getUsers(userDto.getToken());
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertNotNull(errorMessage);
		assertEquals(401, result.getStatus());
		assertEquals(USER_NOT_ENABLED, errorMessage.getDescription());
		assertEquals(DisabledException.class.getSimpleName(), errorMessage.getError());
		assertThatExceptionOfType(DisabledException.class);
		
	}
	
	@Test
	public void testGetAllUsersWithNotAuthorizedRole() throws Exception {

		saveUser(userDto);
		
		MockHttpServletResponse result = getUsers(
				new ObjectMapper().readValue(doLogin(userDto).getContentAsString(), UserDto.class)
				.getToken());
		ErrorMessage errorMessage = new ObjectMapper().readValue(result.getContentAsString(), ErrorMessage.class);
		
		assertEquals(403, result.getStatus());
		assertNotNull(errorMessage);
		assertEquals(ACCESS_DENIED_MESSAGE, errorMessage.getDescription());
		assertThatExceptionOfType(AccessDeniedException.class);
		assertEquals(AccessDeniedException.class.getSimpleName(), errorMessage.getError());
		
	}
	
	@Test
	public void testGetAllUsersIntIntStringString() {
		
	}

	@Test
	public void testRemoveUserById() {
		
	}

	@Test
	public void testUpdateUser() {
		
	}
	
	public MockHttpServletResponse saveUser(UserDto userDto) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_CRUD_USERS)
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(new JSONObject(userDto).toString());
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	public MockHttpServletResponse saveUser(JSONObject jsonObj) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_CRUD_USERS)
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObj.toString());
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	public MockHttpServletResponse getUsers(String token) throws Exception {
		RequestBuilder requestBuilder;
		if(token!=null) {
			requestBuilder = MockMvcRequestBuilders.get(URL_CRUD_USERS).header(HEADER_AUTHORIZATION, JSON_WEB_TOKEN_PREFIX + token);
		} else {
			requestBuilder = MockMvcRequestBuilders.get(URL_CRUD_USERS);
		}
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	public MockHttpServletResponse getUser(String token, Long id) throws Exception {
		RequestBuilder requestBuilder;
		if(token!=null) {
			requestBuilder = MockMvcRequestBuilders.get(URL_CRUD_USERS + "/" + id).header(HEADER_AUTHORIZATION, JSON_WEB_TOKEN_PREFIX + token);
		} else {
			requestBuilder = MockMvcRequestBuilders.get(URL_CRUD_USERS + "/" + id);
		}
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	public MockHttpServletResponse deleteUser(String token, Long id) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL_CRUD_USERS + "/" + id).header(HEADER_AUTHORIZATION, JSON_WEB_TOKEN_PREFIX + token);
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	public MockHttpServletResponse updateUser(UserDto updateUserDto) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(URL_CRUD_USERS + "/" + updateUserDto.getId()).header(HEADER_AUTHORIZATION, JSON_WEB_TOKEN_PREFIX + updateUserDto.getToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(new JSONObject(updateUserDto).toString());
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}
	
	public MockHttpServletResponse doLogin(UserDto userDto) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_LOGIN)
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(new JSONObject(userDto).toString());
		return mockMvc.perform(requestBuilder).andReturn().getResponse();
	}

}
