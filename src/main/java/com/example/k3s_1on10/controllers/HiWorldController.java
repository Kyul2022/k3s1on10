package com.example.k3s_1on10.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class HiWorldController {

	@GetMapping
	public ResponseEntity<String> Hi(){
		
		String hi = "Yo!";
		return ResponseEntity.status(HttpStatus.OK).body(hi);
	}
}
