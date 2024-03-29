package hello.studentgrade;

import hello.studentgrade.models.*;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StudentGradeApplicationTests {

    private static int rootId = 9;
    private static String rootFirstName = "Eric";
    private static String rootLastName = "Roby";
    private static String rootEmail = "test10@naver.com";
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

    @Autowired
    private StudentOne studentOne;

    @BeforeEach
    void setupDatabase() {
        template.update(studentOne.getSqlAddStudent());
        template.update(studentOne.getSqlAddMathGrade());
        template.update(studentOne.getSqlAddHistoryGrade());
        template.update(studentOne.getSqlAddScienceGrade());
    }

    @Test
    void createStudentService() {
        studentService.createStudent("Chad", "Darby", "test9@naver.com");

        CollegeStudent student = studentDao.findByEmailAddress("test9@naver.com");

        assertThat(student.getEmailAddress())
                .isEqualTo("test9@naver.com");
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
        assertThat(studentService.createGrade(80.50, rootId, "math")).isTrue();
        assertThat(studentService.createGrade(80.50, rootId, "science")).isTrue();
        assertThat(studentService.createGrade(80.50, rootId, "history")).isTrue();

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

    @Test
    void studentInformation() {
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(rootId);

        assertThat(gradebookCollegeStudent).isNotNull();
        assertThat(gradebookCollegeStudent.getId()).isEqualTo(rootId);
        assertThat(gradebookCollegeStudent.getFirstname()).isEqualTo(rootFirstName);
        assertThat(gradebookCollegeStudent.getLastname()).isEqualTo(rootLastName);
        assertThat(gradebookCollegeStudent.getEmailAddress()).isEqualTo(rootEmail);
        assertThat(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1).isTrue();
        assertThat(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1).isTrue();
        assertThat(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1).isTrue();
    }

    @Test
    void studentInformationServiceReturnNull() {
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(-1);
        assertThat(gradebookCollegeStudent).isNull();
    }

    @AfterEach
    void setupAfterTransaction() {
        template.execute(studentOne.getSqlDeleteStudent());
        template.execute(studentOne.getSqlDeleteScienceGrade());
        template.execute(studentOne.getSqlDeleteHistoryGrade());
        template.execute(studentOne.getSqlDeleteMathGrade());

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

