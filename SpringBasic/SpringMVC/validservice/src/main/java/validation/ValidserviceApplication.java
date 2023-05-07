package validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import validation.web.validation.ItemValidator;

@SpringBootApplication
public class ValidserviceApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ValidserviceApplication.class, args);
    }

    @Override
    public Validator getValidator() {
        return new ItemValidator();
    }
}
