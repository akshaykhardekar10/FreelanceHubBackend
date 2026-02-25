package com.example.FreelanceX2;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FreelanceX2Application {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().directory("./FreelanceX2").load();
		System.setProperty("API_KEY", dotenv.get("gemini.api.key"));
		System.setProperty("URL", dotenv.get("gemini.api.url"));
		SpringApplication.run(FreelanceX2Application.class, args);
	}

}
