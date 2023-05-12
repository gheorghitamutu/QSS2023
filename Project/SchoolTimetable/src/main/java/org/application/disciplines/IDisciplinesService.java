package org.application.disciplines;

import org.domain.exceptions.discipline.DisciplineAdditionException;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.models.Discipline;

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
