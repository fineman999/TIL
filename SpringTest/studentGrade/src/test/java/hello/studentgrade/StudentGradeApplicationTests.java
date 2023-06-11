package hello.studentgrade;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.MathGrade;
import hello.studentgrade.repository.MathGradesDao;
import hello.studentgrade.repository.StudentDao;
import hello.studentgrade.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StudentGradeApplicationTests {

    private static int rootId = 9;
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @BeforeEach
    void setupDatabase() {
        String sql = "insert into student(id, firstname, lastname, email_address) " +
                "values (?, ?, ?, ?)";
        template.update(sql, rootId, "Eric", "Roby", "test@naver.com");
    }

    @Test
    void createStudentService() {
        studentService.createStudent("Chad", "Darby", "test10@naver.com");

        CollegeStudent student = studentDao.findByEmailAddress("test10@naver.com");

        assertThat(student.getEmailAddress())
                .isEqualTo("test10@naver.com");
    }

    @Test
    void isStudentNullCheck() {
        assertThat(studentService.checkIfStudentIsNull(rootId)).isTrue();

        assertThat(studentService.checkIfStudentIsNull(0)).isFalse();
    }

    @Sql("/insertData.sql")
    @Test
    void getGradebookService() {
        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();
        ArrayList<CollegeStudent> collegeStudents = new ArrayList<>();

        iterableCollegeStudents.iterator().forEachRemaining((collegeStudent -> collegeStudents.add(collegeStudent)));

        assertThat(collegeStudents.size()).isEqualTo(5);
    }

    @Test
    void createGradeService() {
        // Create and Grade
        assertThat(studentService.createGrade(80.50, rootId, "math"));

        // Get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(rootId);

        // Verify there is grades
        assertThat(mathGrades.iterator().hasNext()).isTrue();
    }

    @AfterEach
    void setupAfterTransaction() {
        template.execute("DELETE FROM student");
    }

    @Test
    void deleteStudentService() {
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(rootId);

        assertThat(deletedCollegeStudent.isPresent()).isTrue();

        studentService.deleteStudent(rootId);

        deletedCollegeStudent = studentDao.findById(rootId);

        assertThat(deletedCollegeStudent.isPresent()).isFalse();
    }
}

