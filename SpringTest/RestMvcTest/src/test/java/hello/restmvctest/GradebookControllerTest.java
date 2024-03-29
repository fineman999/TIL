package hello.restmvctest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.restmvctest.models.CollegeStudent;
import hello.restmvctest.models.MathGrade;
import hello.restmvctest.repository.HistoryGradesDao;
import hello.restmvctest.repository.MathGradesDao;
import hello.restmvctest.repository.ScienceGradesDao;
import hello.restmvctest.repository.StudentDao;
import hello.restmvctest.service.StudentAndGradeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class GradebookControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    StudentAndGradeService studentAndGradeService;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CollegeStudent student;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeAll
    static void setup() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Chad");
        request.setParameter("lastname", "Darby");
        request.setParameter("emailAddress", "test@naver.com");
    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.update(sqlAddStudent);
        jdbc.update(sqlAddMathGrade);
        jdbc.update(sqlAddScienceGrade);
        jdbc.update(sqlAddHistoryGrade);
    }

    @Test
    void getStudentsHttpRequest() throws Exception {

        student.setFirstname("Chad");
        student.setLastname("Darby");
        student.setEmailAddress("test@naver.com");
        entityManager.persist(student);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void createStudentHttpRequest() throws Exception {
        student.setFirstname("Chad");
        student.setLastname("Darby");
        student.setEmailAddress("test@naver.com");

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        CollegeStudent verifyStudent = studentDao.findByEmailAddress("test@naver.com");
        assertThat(verifyStudent).as("Student should be valid").isNotNull();

    }

    @Test
    void deleteStudentHttpRequest() throws Exception {
        assertThat(studentDao.findById(9).isPresent()).isTrue();

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 9))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        assertThat(studentDao.findById(9).isPresent()).isFalse();
    }

    @Test
    void deleteStudentHttpRequestErrorPage() throws Exception {
        int notExistedId = 0;
        assertThat(studentDao.findById(notExistedId).isPresent()).isFalse();

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", notExistedId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }

    @Test
    void studentInformationHttpRequest() throws Exception {
        int checkExistedId = 9;
        Optional<CollegeStudent> student = studentDao.findById(checkExistedId);

        assertThat(student.isPresent()).isTrue();

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", checkExistedId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(checkExistedId)))
                .andExpect(jsonPath("$.firstname", is("Eric")))
                .andExpect(jsonPath("$.lastname", is("Roby")))
                .andExpect(jsonPath("$.emailAddress", is("eric.roby@luv2code_school.com")));
    }

    @Test
    void studentInformationHttpRequestEmptyResponse() throws Exception {
        int doesNotExistedIt = 0;
        Optional<CollegeStudent> student = studentDao.findById(doesNotExistedIt);

        assertThat(student.isPresent()).isFalse();

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", doesNotExistedIt))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }

    @Test
    void createAValidGradeHttpRequest() throws Exception {
        int testId = 9;
        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.00")
                        .param("gradeType", "math")
                        .param("studentId", String.valueOf(testId)))
                .andExpect(jsonPath("$.id", is(testId)))
                .andExpect(jsonPath("$.firstname", is("Eric")))
                .andExpect(jsonPath("$.lastname", is("Roby")))
                .andExpect(jsonPath("$.emailAddress", is("eric.roby@luv2code_school.com")))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(2)));

    }

    @Test
    void createAValidGradeHttpRequestStudentDoesNotExistEmptyResponse() throws Exception {
        int NotValidTestId = 0;
        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.00")
                        .param("gradeType", "math")
                        .param("studentId", String.valueOf(NotValidTestId)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }

    @Test
    void createAValidGradeHttpRequestGradeTypeDoesNotExistEmptyResponse() throws Exception {
        int validTestId = 9;
        String NotValidTestGradeType = "literature";
        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.00")
                        .param("gradeType", NotValidTestGradeType)
                        .param("studentId", String.valueOf(validTestId)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));

    }

    @Test
    void deleteAValidGradeHttpRequest() throws Exception {
        int validTestId = 9;
        Optional<MathGrade> mathGrade = mathGradeDao.findById(validTestId);

        assertThat(mathGrade.isPresent()).isTrue();

        mockMvc.perform(delete("/grades/{id}/{gradeType}", validTestId, "math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validTestId)))
                .andExpect(jsonPath("$.firstname", is("Eric")))
                .andExpect(jsonPath("$.lastname", is("Roby")))
                .andExpect(jsonPath("$.emailAddress", is("eric.roby@luv2code_school.com")))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(0)));
    }

    @Test
    void deleteAValidGradeHttpRequestStudentIdDoesNotExistEmptyResponse() throws Exception {

        int notValidTestId = 0;
        mockMvc.perform(delete("/grades/{id}/{gradeType}", notValidTestId, "math"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }

    @Test
    void deleteANonValidGradeHttpRequest() throws Exception {

        int testId = 9;
        String notValidTestGradeType = "literature";
        mockMvc.perform(delete("/grades/{id}/{gradeType}", testId, notValidTestGradeType))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }



    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }
}
