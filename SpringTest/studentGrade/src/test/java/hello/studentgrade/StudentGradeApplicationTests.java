package hello.studentgrade;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.HistoryGrade;
import hello.studentgrade.models.MathGrade;
import hello.studentgrade.models.ScienceGrade;
import hello.studentgrade.repository.HistoryGradesDao;
import hello.studentgrade.repository.MathGradesDao;
import hello.studentgrade.repository.ScienceGradesDao;
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
import java.util.Collection;
import java.util.Collections;
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
    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @BeforeEach
    void setupDatabase() {
        String sql = "insert into student(id, firstname, lastname, email_address) " +
                "values (?, ?, ?, ?)";
        template.update(sql, rootId, "Eric", "Roby", "test@naver.com");

        String mathGradeSql = "insert into math_grade(id, student_id, grade) values (?, ?, ?)";
        template.update(mathGradeSql, 0, rootId, 100.00);

        String scienceGradeSql = "insert into science_grade(id, student_id, grade) values (?, ?, ?)";
        template.update(scienceGradeSql, 0, rootId, 100.00);

        String historyGradeSql = "insert into history_grade(id, student_id, grade) values (?, ?, ?)";
        template.update(historyGradeSql, 0, rootId, 100.00);

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
        assertThat(studentService.createGrade(80.50, rootId, "science"));
        assertThat(studentService.createGrade(80.50, rootId, "history"));

        // Get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(rootId);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(rootId);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(rootId);

        // Verify there is grades
        assertThat(((Collection<MathGrade>) mathGrades).size() == 2).isTrue();
        assertThat(((Collection<ScienceGrade>) scienceGrades).size() == 2).as("Student has science grades").isTrue();
        assertThat(((Collection<HistoryGrade>) historyGrades).size() == 2).as("Student has history grades").isTrue();
    }

    @Test
    void createGradeServiceReturnFalse() {
        assertThat(studentService.createGrade(105, rootId, "math")).isFalse();
        assertThat(studentService.createGrade(-5, rootId, "math")).isFalse();
        assertThat(studentService.createGrade(80.50, 100, "math")).isFalse();
        assertThat(studentService.createGrade(80.50, rootId, "literature")).isFalse();
    }

    @Test
    void deleteGradeService() {
        assertThat(studentService.deleteGrade(0, "math"))
                .as("Returns student id after delete").isEqualTo(rootId);
        assertThat(studentService.deleteGrade(0, "science"))
                .as("Returns student id after delete").isEqualTo(rootId);
        assertThat(studentService.deleteGrade(0, "history"))
                .as("Returns student id after delete").isEqualTo(rootId);
    }

    @Test
    void deleteGradeServiceReturnStudentIdOfZero() {
        assertThat(studentService.deleteGrade(-1, "science"))
                .as("No student should have -1 id").isEqualTo(0);
        assertThat(studentService.deleteGrade(0, "literature"))
                .as("No student should have literature class").isEqualTo(0);

    }

    @AfterEach
    void setupAfterTransaction() {
        template.execute("DELETE FROM student");
        template.execute("DELETE FROM math_grade");
        template.execute("DELETE FROM science_grade");
        template.execute("DELETE FROM history_grade");
    }

    @Test
    void deleteStudentService() {
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(rootId);
        Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(0);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(0);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(0);


        assertThat(deletedCollegeStudent.isPresent()).isTrue();
        assertThat(deletedMathGrade.isPresent()).isTrue();
        assertThat(deletedScienceGrade.isPresent()).isTrue();
        assertThat(deletedHistoryGrade.isPresent()).isTrue();

        studentService.deleteStudent(rootId);

        deletedCollegeStudent = studentDao.findById(rootId);
        deletedMathGrade = mathGradeDao.findById(rootId);
        deletedScienceGrade = scienceGradeDao.findById(rootId);
        deletedHistoryGrade  = historyGradeDao.findById(rootId);

        assertThat(deletedCollegeStudent.isPresent()).isFalse();
        assertThat(deletedMathGrade.isPresent()).isFalse();
        assertThat(deletedScienceGrade.isPresent()).isFalse();
        assertThat(deletedHistoryGrade.isPresent()).isFalse();

    }
}

