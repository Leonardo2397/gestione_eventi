package com.leonardoferrante.gestione_eventi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;


@SpringBootApplication(exclude = {SecurityFilterAutoConfiguration.class})
public class GestioneEventiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestioneEventiApplication.class, args);
	}

}
