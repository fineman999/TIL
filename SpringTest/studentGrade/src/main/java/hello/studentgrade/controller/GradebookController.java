package hello.studentgrade.controller;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.Gradebook;
import hello.studentgrade.models.GradebookCollegeStudent;
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

		GradebookCollegeStudent studentEntity = studentService.studentInformation(id);
		m.addAttribute("student", studentEntity);
		if (studentEntity.getStudentGrades().getMathGradeResults().size() > 0) {
			m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getMathGradeResults()
			));
		} else {
			m.addAttribute("mathAverage", "N/A");
		}

		if (studentEntity.getStudentGrades().getHistoryGradeResults().size() > 0) {
			m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getHistoryGradeResults()
			));
		} else {
			m.addAttribute("historyAverage", "N/A");
		}

		if (studentEntity.getStudentGrades().getScienceGradeResults().size() > 0) {
			m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getScienceGradeResults()
			));
		} else {
			m.addAttribute("scienceAverage", "N/A");
		}



		return "studentInformation";
		}

}
