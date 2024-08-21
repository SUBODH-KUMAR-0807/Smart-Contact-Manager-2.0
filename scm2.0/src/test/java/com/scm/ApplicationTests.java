package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.services.EmailService;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	private EmailService service;


	@Test
	void sendEmailTest(){

		service.sendEmail("6as1940025@gmail.com",
		 "Just Test email",
		    "This is scm2.0 project for testing only");

	}

}
