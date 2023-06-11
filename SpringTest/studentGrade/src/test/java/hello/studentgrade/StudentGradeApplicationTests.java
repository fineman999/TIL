package hello.studentgrade;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.repository.StudentDao;
import hello.studentgrade.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StudentGradeApplicationTests {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @BeforeEach
    void setupDatabase() {
        String sql = "insert into student(id, firstname, lastname, email_address) " +
                "values (?, ?, ?, ?)";
        template.update(sql, 1, "Eric", "Roby", "test@naver.com");
    }

    @Test
    void createStudentService() {
        studentService.createStudent("Chad", "Darby", "test@naver.com");

        CollegeStudent student = studentDao.findByEmailAddress("test@naver.com");

        assertThat(student.getEmailAddress())
                .isEqualTo("test@naver.com");
    }

    @Test
    void isStudentNullCheck() {
        assertThat(studentService.checkIfStudentIsNull(1)).isTrue();

        assertThat(studentService.checkIfStudentIsNull(0)).isFalse();
    }
    @AfterEach
    void setupAfterTransaction() {
        template.execute("DELETE FROM student");
    }
}

