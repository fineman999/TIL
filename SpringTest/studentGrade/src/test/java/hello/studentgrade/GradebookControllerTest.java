package hello.studentgrade;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.GradebookCollegeStudent;
import hello.studentgrade.service.StudentAndGradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentAndGradeServiceMock;

    @BeforeEach
    void beforeEach() {
        String sql = "insert into student(id, firstname, lastname, email_address) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, 1, "Eric", "Roby", "test@naver.com");
    }

    @Test
    void getStudentsHttpRequest() throws Exception {
        GradebookCollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby", "test1@naver.com");
        GradebookCollegeStudent studentTwo = new GradebookCollegeStudent("Chad", "Potter", "test2@naver.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

        for (CollegeStudent collegeStudent : collegeStudentList) {
            log.info("collegeStudentList={}", collegeStudent);
        }

        when(studentAndGradeServiceMock.getGradebook()).thenReturn(collegeStudentList);


        for (CollegeStudent collegeStudent : studentAndGradeServiceMock.getGradebook()) {
            log.info("studentAndGradeServiceMock.getGradebook In={}", collegeStudent);
        }
        assertThat(collegeStudentList).containsExactlyInAnyOrderElementsOf(studentAndGradeServiceMock.getGradebook());

    }

    @AfterEach
    void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM student");
    }

}
