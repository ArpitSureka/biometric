package com.example.biometric;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.biometric.service.StorageService;
import com.example.biometric.storage.StorageProperties;

// @SpringBootApplication
// @ComponentScan(basePackages = {"com.example.biometric.service"})
// @EnableConfigurationProperties(StorageProperties.class)
// @EntityScan("com.example.biometric.model")

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.example.biometric.repository", "com.example.biometric.*"})
@ComponentScan(basePackages = { "com.example.biometric.model", "com.example.biometric.*" })
@EntityScan("com.example.biometric.model")   
@EnableConfigurationProperties(StorageProperties.class)
public class FingerprintRecognitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FingerprintRecognitionApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}