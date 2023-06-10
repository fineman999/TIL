package hello.component.mvctestingexample.config;

import hello.component.mvctestingexample.models.CollegeStudent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class StudentConfig {

    @Bean
    @Scope(value = "prototype")
    CollegeStudent collegeStudent() {
        return new CollegeStudent();
    }
}
