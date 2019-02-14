package snmaddula.logging.poc.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	@GetMapping("/204")
	public ResponseEntity<String> _204() {
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/404")
	public ResponseEntity<String> _404() {
		return ResponseEntity.notFound().build();
	}

	@GetMapping(value="/hello", produces = MediaType.TEXT_PLAIN_VALUE)
	public String hello() {
		return "Hello World";
	}

}
