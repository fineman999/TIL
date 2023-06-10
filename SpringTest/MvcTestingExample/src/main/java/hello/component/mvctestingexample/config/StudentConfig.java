package hello.component.mvctestingexample.config;

import hello.component.mvctestingexample.dao.ApplicationDao;
import hello.component.mvctestingexample.models.CollegeStudent;
import hello.component.mvctestingexample.service.ApplicationService;
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
    /* New for Section 2.2 */
    @Bean
    ApplicationService applicationService() {
        return new ApplicationService();
    }

    /* New for Section 2.2 */
    @Bean
    ApplicationDao applicationDao() {
        return new ApplicationDao();
    }

}
