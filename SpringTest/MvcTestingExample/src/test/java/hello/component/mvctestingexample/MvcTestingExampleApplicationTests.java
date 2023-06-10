package hello.component.mvctestingexample;

import hello.component.mvctestingexample.models.CollegeStudent;
import hello.component.mvctestingexample.models.StudentGrades;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class MvcTestingExampleApplicationTests {

    private static int count = 0;

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.school.name}")
    private String SchoolName;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @BeforeEach
    public void beforeEach() {
        count = count + 1;
        log.info("Testing: {} which is {}", appName, appDescription);
        log.info("Version: {}. Execution of test method {}", appVersion, count);
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("erick.naver.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.5, 91.75)));
        student.setStudentGrades(studentGrades);

    }

    @DisplayName("Add grade results for student grades")
    @Test
    void addGradeResultsForStudentGrades() {
        assertThat(studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()))
                    .isEqualTo(353.25);
    }


    @DisplayName("Add grade results for student grades not equal")
    @Test
    void addGradeResultsForStudentGradesAssertNotEquals() {
        assertThat(studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()))
                .isNotEqualTo(0);
    }

    @DisplayName("Is grade greater")
    @Test
    void isGradeGreaterStudentGrades() {
        assertThat(studentGrades.isGradeGreater(90, 75)).isTrue();
    }

    @DisplayName("Is grade greater false")
    @Test
    void isGradeGreaterStudentGradesAssertFalse() {
        assertThat(studentGrades.isGradeGreater(90, 200)).isFalse();
    }

    @DisplayName("Check Null for student grades")
    @Test
    void checkNullStudentGrades() {
        assertThat(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()))
                .isNotNull();
    }



}
