package hello.component.mvctestingexample;

import hello.component.mvctestingexample.models.CollegeStudent;
import hello.component.mvctestingexample.models.StudentGrades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ReflectionTestUtilsTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

    @BeforeEach
    void studentBeforeEach() {
        studentOne.setFirstname("Eric");
        studentOne.setLastname("Roby");
        studentOne.setEmailAddress("test@naver.com");
        studentOne.setStudentGrades(studentGrades);

        ReflectionTestUtils.setField(studentOne, "id", 1);
        ReflectionTestUtils.setField(studentOne, "studentGrades",
                new StudentGrades(new ArrayList<>(Arrays.asList(
                        100.0, 85.0, 76.50, 91.75))));
    }

    @Test
    void getPrivateField() {
        assertThat(ReflectionTestUtils.getField(studentOne, "id")).isEqualTo(1);
    }

    @Test
    void invokePrivateMethod() {
        assertThat("Eric 1")
                .isEqualTo(ReflectionTestUtils.invokeMethod(studentOne,"getFirstNameAndId"));
    }
}
