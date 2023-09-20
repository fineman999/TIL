package hello.studentgrade.controller;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.Gradebook;
import hello.studentgrade.service.StudentAndGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class GradebookController {

	private final Gradebook gradebook;
	private final StudentAndGradeService studentService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		Iterable<CollegeStudent> collegeStudents = studentService.getGradebook();
		m.addAttribute("students", collegeStudents);
		return "index";
	}


	@PostMapping("/")
	public String createStudent(@ModelAttribute("student") CollegeStudent student, Model model) {
		studentService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
		Iterable<CollegeStudent> collegeStudents = studentService.getGradebook();
		model.addAttribute("students", collegeStudents);


		return "index";
	}


	@DeleteMapping("students/{id}")
	public String deleteStudent(@PathVariable int id, Model model) {
		if (!studentService.checkIfStudentIsNull(id)) {
			return "error";
		}
		studentService.deleteStudent(id);
		Iterable<CollegeStudent> collegeStudents = studentService.getGradebook();
		model.addAttribute("students", collegeStudents);
		return "index";
	}

	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model m) {
		if (!studentService.checkIfStudentIsNull(id)) {
			return "error";
		}

		studentService.configureStudentInformationModel(id, m);

		return "studentInformation";
	}

	@PostMapping("/grades")
	public String createGrade(@RequestParam("grade") double grade,
							  @RequestParam("gradeType") String gradeType,
							  @RequestParam("studentId") int studentId,
							  Model m) {
		if (!studentService.checkIfStudentIsNull(studentId)) {
			return "error";
		}
		boolean success = studentService.createGrade(grade, studentId, gradeType);

		if (!success) {
			return "error";
		}

		studentService.configureStudentInformationModel(studentId, m);

		return "studentInformation";
	}

	@DeleteMapping("/grades/{id}/{gradeType}")
	public String deleteGrade(@PathVariable int id, @PathVariable String gradeType, Model model) {
		int studentId = studentService.deleteGrade(id, gradeType);

		if (studentId == 0) {
			return "error";
		}

		studentService.configureStudentInformationModel(studentId, model);

		return "studentInformation";
	}

}
