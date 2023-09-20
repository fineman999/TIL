package hello.studentgrade.service;

import hello.studentgrade.models.CollegeStudent;
import hello.studentgrade.models.MathGrade;
import hello.studentgrade.models.ScienceGrade;
import hello.studentgrade.repository.MathGradesDao;
import hello.studentgrade.repository.ScienceGradesDao;
import hello.studentgrade.repository.StudentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentAndGradeService {

    private final StudentDao studentDao;

    @Qualifier("mathGrades")
    private final MathGrade mathGrade;

    @Qualifier("scienceGrades")
    private final ScienceGrade scienceGrade;

    private final MathGradesDao mathGradesDao;
    private final ScienceGradesDao scienceGradesDao;
    public void createStudent(String firstname, String lastname, String emailAddress) {
        CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
//        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);
        if (student.isPresent()) {
            return true;
        }
        return false;
    }

    public void deleteStudent(int id) {
        if (checkIfStudentIsNull(id)) {
            studentDao.deleteById(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
        return collegeStudents;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {
        if (!checkIfStudentIsNull(studentId)) {
            return false;
        }
        if (grade >= 0 && grade <= 100) {
            if (gradeType.equals("math")) {
//                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradesDao.save(mathGrade);
                return true;
            }

            if (gradeType.equals("science")) {
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradesDao.save(scienceGrade);
                return true;
            }


        }
        return false;
    }
}
