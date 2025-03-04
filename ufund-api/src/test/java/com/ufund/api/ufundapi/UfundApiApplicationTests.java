package com.ufund.api.ufundapi;

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

import com.ufund.api.ufundapi.controller.LoginController;

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
	
	/**
	 * Runs before each test to make new LoginController and a new session.
	 */
	@BeforeEach
        @SuppressWarnings("unused")
	void start(){
		session = new MockHttpSession();
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
	void testManagerLogin(){
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
	void testHelperLogin(){
		Map<String, String> response = loginController.login(
			Map.of("username", "user1", "password", "helper123"), session
			);
		assertEquals("Login successful", response.get("message"));
		assertEquals("Helper", response.get("role"));
		assertEquals("Helper", session.getAttribute("role"));

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

}
