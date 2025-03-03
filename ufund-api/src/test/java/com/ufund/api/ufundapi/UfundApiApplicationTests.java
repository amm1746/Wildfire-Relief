package com.ufund.api.ufundapi;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import com.ufund.api.ufundapi.controller.LoginController;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
class UfundApiApplicationTests {

	private LoginController loginController;
	private HttpSession session;
	
	@BeforeEach
        @SuppressWarnings("unused")
	void start(){
		loginController = new LoginController();
		session = new MockHttpSession();
	}

	@Test
	void contextLoads() {
		assertTrue(true);
	}

	@Test
	void testManagerLogin(){
		Map<String, String> response = loginController.login(
			Map.of("username", "admin", "password", "admin123"), session
			);
		assertEquals("Login sucessful", response.get("message"));
		assertEquals("U-Fund Manager", response.get("role"));
		assertEquals("U-Fund Manager", session.getAttribute("role"));

	}

	@Test
	void testHelperLogin(){
		Map<String, String> response = loginController.login(
			Map.of("username", "user1", "password", "helper123"), session
			);
		assertEquals("Login sucessful", response.get("message"));
		assertEquals("Helper", response.get("role"));
		assertEquals("Helper", session.getAttribute("role"));

	}

	@Test
	void testGetRole(){
		session.setAttribute("role", "U-Fund Manager");
		Map<String, String> response = loginController.getRole(session);
		assertEquals("U-Fund Manager", response.get("role"));

	}

	@Test
	void testLogout(){
		session.setAttribute("role", "U-Fund Manager");
		Map<String, String> response = loginController.logout(session);
		assertEquals("Logged out successfully", response.get("message"));
		assertNull(session.getAttribute("role"));
	}

}
