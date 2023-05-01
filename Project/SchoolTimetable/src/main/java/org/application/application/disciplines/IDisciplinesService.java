package org.application.application.disciplines;

import org.application.domain.exceptions.*;
import org.application.domain.models.Discipline;
import org.application.domain.models.StudentGroup;

import java.util.List;

public interface IDisciplinesService {

    public Discipline addDiscipline(String name, int credits) throws DisciplineAdditionException;

    public boolean deleteDiscipline(int disciplineId) throws DisciplineNotFoundException, DisciplineDeletionFailed;

    public Discipline getDisciplineById(int disciplineId) throws DisciplineNotFoundException;

    public List<Discipline> getDisciplines();
}
