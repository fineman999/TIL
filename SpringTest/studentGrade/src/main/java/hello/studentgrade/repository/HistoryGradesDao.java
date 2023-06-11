package hello.studentgrade.repository;

import hello.studentgrade.models.HistoryGrade;
import hello.studentgrade.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradesDao extends CrudRepository<HistoryGrade, Integer> {
    public Iterable<HistoryGrade> findGradeByStudentId(int id);
}
