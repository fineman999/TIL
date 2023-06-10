package hello.studentgrade;

import hello.studentgrade.service.StudentAndGradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentGradeApplicationTests {

    @Autowired
    private StudentAndGradeService studentService;
    @Test
    void createStudentService() {
        studentService.createStudent("Chad", "Darby", "test@naver.com");

//        studentDao.findByEmailAddress("test@naver.com");
//
//        Assertions.assertThat(student.getEmailAddress())
//                .isEqal
    }

}
