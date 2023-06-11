package hello.studentgrade;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.GradebookCollegeStudent;
import hello.studentgrade.repository.StudentDao;
import hello.studentgrade.service.StudentAndGradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentAndGradeServiceMock;

    @Autowired
    private StudentDao studentDao;

    @BeforeAll
    static void setup() {
         request = new MockHttpServletRequest();
         request.setParameter("firstname", "Chad");
         request.setParameter("lastname", "Darby");
         request.setParameter("emailAddress", "test3@naver.com");
    }

    @BeforeEach
    void beforeEach() {
        String sql = "insert into student(id, firstname, lastname, email_address) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, 4, "Eric", "Roby", "test@naver.com");
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

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");
    }

    @Test
    void createStudentHttpRequest() throws Exception{

        GradebookCollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby", "test1@naver.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne));

        when(studentAndGradeServiceMock.getGradebook()).thenReturn(collegeStudentList);

        assertThat(collegeStudentList).containsExactlyInAnyOrderElementsOf(studentAndGradeServiceMock.getGradebook());



        MvcResult mvcResult = this.mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstname", request.getParameterValues("firstname"))
                        .param("lastname", request.getParameterValues("lastname"))
                        .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        CollegeStudent verifyStudent = studentDao.findByEmailAddress("test3@naver.com");

        assertThat(verifyStudent).as("Student should be found").isNotNull();
    }

    @Test
    void deleteStudentHttpRequest() throws Exception {
        // 실제 값이 있는지 확인
        assertThat(studentDao.findById(4).isPresent()).isTrue();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", 4))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");
        // 삭제 후 없는지 확인
        assertThat(studentDao.findById(4).isPresent()).isFalse();

    }

    @AfterEach
    void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM student");
    }



}
