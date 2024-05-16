package com.nanda.betest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	
	@GetMapping("/")
	public String home() throws Exception {
		String response = "";
		try {
			response = "<h3>Test OK</>";
		} catch (Exception e) {
			response = "<h3>Koneksi Gagal</>";
			e.printStackTrace();
		}
		return response;
	}

}
