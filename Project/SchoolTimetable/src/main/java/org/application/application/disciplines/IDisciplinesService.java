package org.application.application.disciplines;

import org.application.domain.exceptions.discipline.DisciplineAdditionException;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.Discipline;

import java.util.List;

public interface IDisciplinesService {

    public Discipline addDiscipline(String name, int credits) throws DisciplineAdditionException;

    public boolean deleteDiscipline(int disciplineId) throws DisciplineNotFoundException, DisciplineDeletionFailed;

    public boolean deleteDisciplines(String name) throws DisciplineDeletionFailed;

    public boolean deleteAll() throws DisciplineDeletionFailed;

    public Discipline getDisciplineById(int disciplineId) throws DisciplineNotFoundException;

    public List<Discipline> getDisciplines();

    public Discipline addTeacherToDiscipline(String teacherName, String disciplineName) throws TeacherNotFoundException, DisciplineNotFoundException;
}
