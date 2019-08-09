package javatp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javatp.entities.User;
import javatp.logic.UserLogic;
import javatp.repositories.POIRepository;
import javatp.repositories.UserRepository;
import javatp.util.AuthenticationManager;
import javatp.util.Message;
import javatp.entities.POI;

@SpringBootApplication
@RestController
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	@Autowired private POIRepository poiRepository;
	@Autowired private UserLogic users;

	@Autowired
	private AuthenticationManager auth;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostMapping(value = "/poi")
	public ResponseEntity<Object> createPOI(@RequestBody POI poi,
			@RequestHeader(value = "Authorization") String token) {
		try {
			logger.info(poi.toString());
			auth.authenticateToken(token);
			poiRepository.save(poi);
			return ResponseEntity.ok(poi);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new Message(e.getMessage(), null));
		}
	}

	@GetMapping(value = "/pois")
	public ResponseEntity<Object> getPOIs(@RequestHeader(value = "Authorization") String token) {
		try {
			auth.authenticateToken(token);
			return ResponseEntity.ok(poiRepository.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new Message(e.getMessage(), null));
		}
	}

	@PostMapping(value = "/auth")
	public ResponseEntity<Object> auth(@RequestBody User user) {
		try {
			String token = auth.generateToken(user);
			logger.info(token);
			return new ResponseEntity<>(token, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new Message(e.getMessage(), null));
		}
	}

	@PostMapping(value = "/user")
	public ResponseEntity<Object> createUser(@RequestBody User user) {
		try {
			user = users.save(user);
			return new ResponseEntity<>(new Message(null,user.getId().toString()), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new Message(e.getMessage(), null));
		}
	}

	@GetMapping(value = "/users")
	public ResponseEntity<Object> getUsers(@RequestHeader(value = "Authorization") String token) {
		try {
			auth.authenticateToken(token);
			return ResponseEntity.ok(users.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new Message(e.getMessage(), null));
		}
	}

}