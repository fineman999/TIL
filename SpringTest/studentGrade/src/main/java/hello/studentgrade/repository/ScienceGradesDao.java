package hello.studentgrade.repository;

import hello.studentgrade.models.MathGrade;
import hello.studentgrade.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradesDao extends CrudRepository<ScienceGrade, Integer> {
    public Iterable<ScienceGrade> findGradeByStudentId(int id);
}
