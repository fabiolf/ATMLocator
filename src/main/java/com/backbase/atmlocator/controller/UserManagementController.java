package com.backbase.atmlocator.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class UserManagementController {
	private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@RequestMapping(value = "/api/user/authenticate", method = { POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> authenticate(@RequestParam("user") String username,
			@RequestParam("password") String password) throws JsonProcessingException {
		System.out.println("Authenticate with user " + username + " and password " + password + "!");
		logger.info("Received user ".concat(username).concat(" with password ").concat(password)
				.concat(" to be authenticated!"));
		
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// prepare the response as a JSON object
		// sending the password as the token, just to test the process... I know... crappy token
		return new ResponseEntity<String>(password, HttpStatus.OK);
	}

}
