package com.ufund.api.ufundapi;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
