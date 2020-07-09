package pl.Pakinio.KursUdemy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class KursUdemyApplication {

	public static void main(String[] args) {
		SpringApplication.run(KursUdemyApplication.class, args);
	}


	@Bean
	Validator validaror() {
		return new LocalValidatorFactoryBean();
	}
}
