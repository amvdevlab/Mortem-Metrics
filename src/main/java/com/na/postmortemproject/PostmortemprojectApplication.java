package com.na.postmortemproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PostmortemprojectApplication {

	public static void main(String[] args) {
		// Load .env file and set system properties
		try {
			Dotenv dotenv = Dotenv.configure()
					.directory("./")
					.ignoreIfMissing()
					.load();
			
			dotenv.entries().forEach(e -> {
				System.setProperty(e.getKey(), e.getValue());
			});
			
			System.out.println("✓ Loaded environment variables from .env file");
		} catch (Exception e) {
			System.out.println("⚠ Could not load .env file: " + e.getMessage());
			System.out.println("  Using system environment variables instead");
		}
		
		SpringApplication.run(PostmortemprojectApplication.class, args);
	}

}

