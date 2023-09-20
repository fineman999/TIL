package hello.restmvctest.controller;

import hello.restmvctest.exceptionhandling.StudentOrGradeErrorResponse;
import hello.restmvctest.exceptionhandling.StudentOrGradeNotFoundException;
import hello.restmvctest.models.CollegeStudent;
import hello.restmvctest.models.Gradebook;
import hello.restmvctest.models.GradebookCollegeStudent;
import hello.restmvctest.service.StudentAndGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GradebookController {

    private final StudentAndGradeService studentService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<GradebookCollegeStudent> getStudents() {
        Gradebook gradebook = studentService.getGradebook();
        return gradebook.getStudents();
    }


    @GetMapping("/studentInformation/{id}")
    public GradebookCollegeStudent studentInformation(@PathVariable int id) {

        if (!studentService.checkIfStudentIsNull(id)) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }
        GradebookCollegeStudent studentEntity = studentService.studentInformation(id);

        return studentEntity;
    }


    @PostMapping(value = "/")
    public List<GradebookCollegeStudent> createStudent(@RequestBody CollegeStudent student) {

        studentService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
        Gradebook gradebook = studentService.getGradebook();
        return gradebook.getStudents();
    }


    @DeleteMapping("/student/{id}")
    public List<GradebookCollegeStudent> deleteStudent(@PathVariable int id) {

        if (!studentService.checkIfStudentIsNull(id)) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        studentService.deleteStudent(id);
        Gradebook gradebook = studentService.getGradebook();
        return gradebook.getStudents();
    }


    @PostMapping(value = "/grades")
    public GradebookCollegeStudent createGrade(@RequestParam("grade") double grade,
                                               @RequestParam("gradeType") String gradeType,
                                               @RequestParam("studentId") int studentId) {

        if (!studentService.checkIfStudentIsNull(studentId)) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        boolean success = studentService.createGrade(grade, studentId, gradeType);

        if (!success) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        GradebookCollegeStudent studentEntity = studentService.studentInformation(studentId);

        if (studentEntity == null) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        return studentEntity;
    }

    @DeleteMapping("/grades/{id}/{gradeType}")
    public GradebookCollegeStudent deleteGrade(@PathVariable int id, @PathVariable String gradeType) {

        int studentId = studentService.deleteGrade(id, gradeType);

        if (studentId == 0) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        GradebookCollegeStudent studentEntity = studentService.studentInformation(studentId);

        return studentEntity;
    }

    @ExceptionHandler
    public ResponseEntity<StudentOrGradeErrorResponse> handleException(StudentOrGradeNotFoundException exc) {

        StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentOrGradeErrorResponse> handleException(Exception exc) {

        StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
