package hello.studentgrade;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentGradeApplicationTests {

    @Test
    void createStudentService() {
        studentService.createStudent("Chad", "Darby", "test@naver.com");

//        studentDao.findByEmailAddress("test@naver.com");
//
//        Assertions.assertThat(student.getEmailAddress())
//                .isEqal
    }

}
