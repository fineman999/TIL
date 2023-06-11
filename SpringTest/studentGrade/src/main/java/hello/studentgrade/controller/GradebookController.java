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
		return "index";
	}

	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model m) {
		return "studentInformation";
		}

}
