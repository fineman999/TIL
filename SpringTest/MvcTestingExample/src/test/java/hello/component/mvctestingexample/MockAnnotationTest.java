package hello.component.mvctestingexample;

import hello.component.mvctestingexample.dao.ApplicationDao;
import hello.component.mvctestingexample.models.CollegeStudent;
import hello.component.mvctestingexample.models.StudentGrades;
import hello.component.mvctestingexample.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
public class MockAnnotationTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

    @MockBean
    private ApplicationDao applicationDao;

    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    void beforeEach() {
        studentOne.setFirstname("Eric");
        studentOne.setLastname("Roby");
        studentOne.setEmailAddress("test@naver.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    void assertEqualsTestAddGrades() {
        when(applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults()))
                .thenReturn(100.00);

        assertThat(applicationService.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults()
        )).isEqualTo(100);

        verify(applicationDao, times(1))
                .addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @DisplayName("Find Gpa")
    @Test
    void assertEqualsTestFindGpa() {
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.31);

        assertThat(applicationService.findGradePointAverage(studentOne.getStudentGrades().getMathGradeResults()))
                .isEqualTo(88.31);

        verify(applicationDao, times(1))
                .findGradePointAverage(studentOne.getStudentGrades().getMathGradeResults());
    }

    @DisplayName("Not Null")
    @Test
    void testAssertNotNull() {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
                .thenReturn(true);
        assertThat(applicationService.checkNull(studentOne.getStudentGrades().getMathGradeResults()))
                .isNotNull();
    }

    @DisplayName("Throw runtime error")
    @Test
    void throwRuntimeError() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);
        assertThatThrownBy(() -> applicationService.checkNull(nullStudent))
                .isInstanceOf(RuntimeException.class);

        verify(applicationDao, times(1))
                .checkNull(nullStudent);
    }

    @DisplayName("Multiple Stubbing")
    @Test
    void stubbingConsecutiveCalls() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw exception second time");

        assertThatThrownBy(() -> applicationService.checkNull(nullStudent))
                .isInstanceOf(RuntimeException.class);

        assertThat(applicationService.checkNull(nullStudent))
                .isEqualTo("Do not throw exception second time");

        verify(applicationDao, times(2))
                .checkNull(nullStudent);


    }

}
