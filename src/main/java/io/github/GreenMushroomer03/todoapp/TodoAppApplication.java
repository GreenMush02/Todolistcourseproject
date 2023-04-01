package io.github.GreenMushroomer03.todoapp;

import jakarta.validation.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

// Możemy zaimportować klasę konfiguracyjną (np. jeżeli jest umieszczona w innym niż głównym pakiecie)
//@Import(TaskConfigurationProperties.class)
@SpringBootApplication
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}


	@Bean
	Validator validator(){
		return new LocalValidatorFactoryBean();
	}

}
