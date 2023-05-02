package org.application.application.disciplines;

import com.google.inject.Inject;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.domain.exceptions.discipline.DisciplineAdditionException;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;

import java.text.MessageFormat;
import java.util.List;

public class DisciplinesService implements IDisciplinesService {

    private final IDisciplineRepository disciplineRepository;

    @Inject
    public DisciplinesService(IDisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    @Override
    public Discipline addDiscipline(String name, int credits) throws DisciplineAdditionException {
        Discipline discipline = null;

        try {
            discipline = disciplineRepository.getByName(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (discipline == null) {
            try {
                discipline = disciplineRepository.createNewDiscipline(name, credits);
            } catch (RepositoryOperationException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            disciplineRepository.save(discipline);
        } catch (Exception e) {
            throw new DisciplineAdditionException("[DisciplineService] Failed adding group!", e);
        }

        return discipline;
    }

    @Override
    public boolean deleteDiscipline(int disciplineId) throws DisciplineNotFoundException, DisciplineDeletionFailed {
        var discipline = disciplineRepository.getById(disciplineId);
        if (discipline == null) {
            throw new DisciplineNotFoundException(MessageFormat.format("[DisciplineService] Discipline with id {0} not found.", disciplineId));
        }

        try {
            disciplineRepository.delete(discipline);
        } catch (Exception e) {
            throw new DisciplineDeletionFailed(" [DisciplineService] Failed to delete discipline.", e);
        }

        return true;
    }

    @Override
    public boolean deleteAll() throws DisciplineDeletionFailed {
        try {
            disciplineRepository.deleteMany(disciplineRepository.readAll());
        } catch (Exception e) {
            throw new DisciplineDeletionFailed(" [DisciplineService] Failed to delete disciplines.", e);
        }

        return true;
    }

    @Override
    public Discipline getDisciplineById(int disciplineId) throws DisciplineNotFoundException {
        var discipline = disciplineRepository.getById(disciplineId);
        if (discipline == null) {
            throw new DisciplineNotFoundException(MessageFormat.format("[DisciplineService] Student Group with id {0} not found.", disciplineId));
        }
        return discipline;
    }

    @Override
    public List<Discipline> getDisciplines() {
        return disciplineRepository.readAll();
    }
}
