package hello.studentgrade;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.repository.StudentDao;
import hello.studentgrade.service.StudentAndGradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StudentGradeApplicationTests {

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Test
    void createStudentService() {
        studentService.createStudent("Chad", "Darby", "test@naver.com");

        CollegeStudent student = studentDao.findByEmailAddress("test@naver.com");

        assertThat(student.getEmailAddress())
                .isEqualTo("test@naver.com");
    }

}
