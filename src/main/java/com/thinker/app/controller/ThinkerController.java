package com.thinker.app.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thinker.app.entity.AuthRequest;
import com.thinker.app.entity.AuthResponse;
import com.thinker.app.entity.Users;
import com.thinker.app.entity.GetIdentifierResponse;
import com.thinker.app.entity.ResetIdentifierRequest;
import com.thinker.app.repository.UserRepository;
import com.thinker.app.util.JWTUtil;

@RestController
public class ThinkerController {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserRepository repository;

	@Autowired
	private AuthenticationManager authentiactionManager;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	@GetMapping("/")
	public String entryPoint(Principal principal) {
		Users currentUser = repository.findByEmail(principal.getName());

		return "Welcome home " + currentUser.getFullname() + "!!";
	}

	
	@GetMapping(value = "/current", produces = "application/json")
	public GetIdentifierResponse getuseridentifier(Principal principal) {
		Users currentUser = repository.findByEmail(principal.getName());
		GetIdentifierResponse response = new GetIdentifierResponse();
		response.setEmail(currentUser.getEmail());
		response.setFullname(currentUser.getFullname());
		response.setIdentifier(currentUser.getIdentifier());

		return response;
	}

	@RequestMapping(value = "/current", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public GetIdentifierResponse putIdentifier(Principal principal, @RequestBody final ResetIdentifierRequest request) {

		Users currentUser = repository.findByEmail(principal.getName());
		currentUser.setIdentifier(request.getIdentifier());
		repository.save(currentUser);
		GetIdentifierResponse response = new GetIdentifierResponse();

		response.setEmail(currentUser.getEmail());
		response.setFullname(currentUser.getFullname());
		response.setIdentifier(currentUser.getIdentifier());

		return response;
	}

	@GetMapping(value = "/next", produces = "application/json")
	public GetIdentifierResponse nextIdentifier(Principal principal) {

		Users currentUser = repository.findByEmail(principal.getName());

		currentUser.setIdentifier(currentUser.getIdentifier() + 1);
		repository.save(currentUser);
		GetIdentifierResponse response = new GetIdentifierResponse();

		response.setEmail(currentUser.getEmail());
		response.setFullname(currentUser.getFullname());
		response.setIdentifier(currentUser.getIdentifier());

		return response;
	}

	
	
	@PostMapping(value = "/authenticate", consumes = "application/json", produces = "application/json")
	public Object generateToken(@RequestBody AuthRequest authRequest) throws Exception {
		try {
			authentiactionManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		AuthResponse response = new AuthResponse();
		response.setToken(jwtUtil.generateToken(authRequest.getEmail()));

		return response;
	}

	@PostMapping(value = "/signup" , consumes = "application/json", produces = "application/json")
	public Object generateSignup(@RequestBody Users signupRequest) throws Exception {
				Users user = repository.findByEmail(signupRequest.getEmail());

		if (user != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

		repository.save(signupRequest);

		AuthResponse response = new AuthResponse();
		response.setToken(jwtUtil.generateToken(signupRequest.getEmail()));

		return response;
	}

}
