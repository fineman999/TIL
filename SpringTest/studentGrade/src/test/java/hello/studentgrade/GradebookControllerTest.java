package hello.studentgrade;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.GradebookCollegeStudent;
import hello.studentgrade.models.MathGrade;
import hello.studentgrade.repository.MathGradesDao;
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
import java.util.Optional;

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
    private JdbcTemplate template;

    @Autowired
    private StudentOne studentOne;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentAndGradeServiceMock;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentAndGradeService studentAndGradeService;

    @Autowired
    private MathGradesDao mathGradeDao;

    @BeforeAll
    static void setup() {
         request = new MockHttpServletRequest();
         request.setParameter("firstname", "Chad");
         request.setParameter("lastname", "Darby");
         request.setParameter("emailAddress", "test3@naver.com");
    }

    @BeforeEach
    void setupDatabase() {
        template.update(studentOne.getSqlAddStudent());
        template.update(studentOne.getSqlAddMathGrade());
        template.update(studentOne.getSqlAddHistoryGrade());
        template.update(studentOne.getSqlAddScienceGrade());
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
        assertThat(studentDao.findById(9).isPresent()).isTrue();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", 9))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");
        // 삭제 후 없는지 확인
        assertThat(studentDao.findById(9).isPresent()).isFalse();

    }

    @Test
    void deleteStudentHttpRequestErrorPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void studentInformationHttpRequest() throws Exception{
        assertThat(studentDao.findById(9).isPresent()).isTrue();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 9))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "studentInformation");
    }

    @Test
    void studentInformationHttpStudentDoesNotExistRequest() throws Exception {
        assertThat(studentDao.findById(-1).isPresent()).isFalse();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", -1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void createValidGradeHttpRequest() throws Exception {
        assertThat(studentDao.findById(9).isPresent()).isTrue();

        GradebookCollegeStudent student = studentAndGradeService.studentInformation(9);

        assertThat(student.getStudentGrades().getMathGradeResults().size()).isEqualTo(1);

        MvcResult mvcResult = mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.00")
                        .param("gradeType", "math")
                        .param("studentId", "9"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "studentInformation");

        student = studentAndGradeService.studentInformation(9);

        assertThat(student.getStudentGrades().getMathGradeResults().size()).isEqualTo(2);

    }

    @Test
    void createAValidGradeHttpRequestStudentDoesNotExistEmptyResponse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.00")
                        .param("gradeType", "history")
                        .param("studentId", "-1"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "error");
    }

    @Test
    void createANonValidGradeHttpRequestGradeTypeDoesNotExistEmptyResponse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.00")
                        .param("gradeType", "literature")
                        .param("studentId", "9"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "error");
    }

    @Test
    void deleteAValidGradeHttpRequest() throws Exception {
        Optional<MathGrade> mathGrade = mathGradeDao.findById(0);
        assertThat(mathGrade.isPresent()).isTrue();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/grades/{id}/{gradeType}", 0, "math"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "studentInformation");
        mathGrade = mathGradeDao.findById(0);
        assertThat(mathGrade.isPresent()).isTrue();

    }

    @Test
    void deleteAValidGradeHttpRequestStudentIdDoesNotExistEmptyResponse() throws Exception {
        int NotExistStudentId = 100;
        Optional<MathGrade> mathGrade = mathGradeDao.findById(NotExistStudentId);
        assertThat(mathGrade.isPresent()).isFalse();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/grades/{id}/{gradeType}", NotExistStudentId, "math"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "error");
    }

    @Test
    void deleteANonValidGradeHttpRequest() throws Exception {
        int studentId = 9;
        String nonValidGradeType = "literature";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/grades/{id}/{gradeType}", studentId, nonValidGradeType))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "error");

    }



    @AfterEach
    void setupAfterTransaction() {
        template.execute(studentOne.getSqlDeleteStudent());
        template.execute(studentOne.getSqlDeleteScienceGrade());
        template.execute(studentOne.getSqlDeleteHistoryGrade());
        template.execute(studentOne.getSqlDeleteMathGrade());
    }


}
