package com.ufund.api.ufundapi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.controller.LoginController;
import com.ufund.api.ufundapi.dao.UserFileDAO;
import com.ufund.api.ufundapi.model.User;

import jakarta.servlet.http.HttpSession;
/**
 * JUnit Tests for UfundAPIApplication
 * 
 * @author Alexandra Mantagas
 */

@SpringBootTest
@TestPropertySource(properties = "admin.password=admin123")
class UfundApiApplicationTests {

	@Autowired
	private LoginController loginController;
	
	private HttpSession session;

	private UserFileDAO testUserFileDAO;
	
	/**
	 * Runs before each test to make new LoginController and a new session.
	 */
	@BeforeEach
        @SuppressWarnings("unused")
	void start() throws IOException {
		session = new MockHttpSession();

		File tempUserFile = File.createTempFile("test-users", ".json");
		tempUserFile.deleteOnExit();
		Files.writeString(tempUserFile.toPath(), "[]");
		testUserFileDAO = new UserFileDAO(tempUserFile.getAbsolutePath(), new ObjectMapper());

		loginController = new LoginController(testUserFileDAO);
		ReflectionTestUtils.setField(loginController, "ADMIN_PASSWORD", "admin123");
	}

	/**
	 * Checks that the application starts without errors.
	 */
	@Test
	void contextLoads() {
		assertTrue(true);
	}

	/**
	 * Tests Manager login by calling login() method with admin credentials,
	 * checks that response contains "Login successful", and ensures that
	 * session correcrtly stores the role "U-Fund Manager".
	 */
	@Test
	void testManagerLogin() throws IOException{
		Map<String, String> response = loginController.login(
			Map.of("username", "admin", "password", "admin123"), session
			);
		assertEquals("Login successful", response.get("message"));
		assertEquals("U-Fund Manager", response.get("role"));
		assertEquals("U-Fund Manager", session.getAttribute("role"));

	}

	/**
	 * Tests Helper login by calling login() method with helper credentials,
	 * checks that response contains "Login successful", and ensures that
	 * session correcrtly stores the role "Helper".
	 */
	@Test
	void testHelperLogin() throws IOException{
		loginController.register(
			Map.of("username", "user1", "password", "helper123")
		);
		
		Map<String, String> response = loginController.login(
			Map.of("username", "user1", "password", "helper123"), session
			);
		assertEquals("Login successful", response.get("message"));
		assertEquals("Helper", response.get("role"));
		assertEquals("Helper", session.getAttribute("role"));

	}

	@Test
	void testHelperRegistration() throws IOException{
		Map<String, String> response = loginController.register(
			Map.of("username", "new_helper", "password", "pass123")
		);

		assertEquals("Helper account created successfully", response.get("message"));
		assertEquals("Helper", response.get("role"));

		Map<String, String> loginResponse = loginController.login(
			Map.of("username", "new_helper", "password", "pass123"), session
		);

		assertEquals("Login successful", loginResponse.get("message"));
		assertEquals("Helper", loginResponse.get("role"));
	}

	@Test
	void testNonexistentUser() throws IOException{
		assertNull(testUserFileDAO.getUser("nope"));
	}

	@Test
	void testGetAllUsersEmptyInitially() throws IOException {
    List<User> users = testUserFileDAO.getAllUsers();
    assertTrue(users.isEmpty());
	}

	/**
	 * Tests getRole() by manually setting the role, calling getRole(),
	 * and checking that the returned role matches what was set.
	 */
	@Test
	void testGetRole(){
		session.setAttribute("role", "U-Fund Manager");
		Map<String, String> response = loginController.getRole(session);
		assertEquals("U-Fund Manager", response.get("role"));

	}

	/**
	 * Tests logout by manually setting the role, calling logout, and 
	 * ensuring "Logged out successfully" is returned as well as the role
	 * being removed from the session.
	 */
	@Test
	void testLogout(){
		session.setAttribute("role", "U-Fund Manager");
		Map<String, String> response = loginController.logout(session);
		assertEquals("Logged out successfully", response.get("message"));
		assertNull(session.getAttribute("role"));
	}

	@Test
    void testMainMethod() {
        UfundApiApplication.main(new String[] {}); 
    }

}
