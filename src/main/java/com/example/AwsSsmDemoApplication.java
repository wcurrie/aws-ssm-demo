package com.example;

import com.example.db.Person;
import com.example.db.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AwsSsmDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsSsmDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner removePeople(PersonRepository repository) {
		return strings -> {
            repository.deleteAll();
        };
	}

	@Bean
	public CommandLineRunner makePeople(PersonRepository repository) {
		return strings -> {
            repository.save(new Person("John"));
            repository.save(new Person("Mary"));
            repository.save(new Person("Bob"));
            repository.save(new Person("Jane"));
        };
	}

	@Bean
	public CommandLineRunner printPeople(PersonRepository repository) {
		return strings -> {
			for (Person person : repository.findAll()) {
				System.out.println(person);
			}
            System.out.println("SUCCESS!");
		};
	}
}
