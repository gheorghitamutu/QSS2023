package org.application.disciplines;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.discipline.IDisciplineRepository;
import org.dataaccess.teacher.ITeacherRepository;
import org.domain.exceptions.discipline.DisciplineAdditionException;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Discipline;

import java.text.MessageFormat;
import java.util.List;

public class DisciplinesService implements IDisciplinesService {

    private final IDisciplineRepository disciplineRepository;
    private final ITeacherRepository teacherRepository;

    @Inject
    public DisciplinesService(IDisciplineRepository disciplineRepository, ITeacherRepository teacherRepository) {
        this.disciplineRepository = disciplineRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Discipline addDiscipline(String name, int credits) throws DisciplineAdditionException, ValidationException {
        Discipline discipline = null;

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[Discipline Service] Discipline name is invalid", null);
        ValidationHelpers.requirePositiveOrZero(credits, IllegalArgumentException.class, "[Discipline Service] Discipline credits value provided is negative", null);

        try {
            discipline = disciplineRepository.getByName(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (discipline == null) {
            try {
                discipline = disciplineRepository.createNewDiscipline(name, credits);
            } catch (RepositoryOperationException | ValidationException e) {
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
    public boolean deleteDiscipline(int disciplineId) throws DisciplineNotFoundException, DisciplineDeletionFailed, ValidationException {

        ValidationHelpers.requirePositiveOrZero(disciplineId, IllegalArgumentException.class, "[Discipline Service] Discipline id is invalid", null);

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
    public boolean deleteDisciplines(String name) throws DisciplineDeletionFailed {

        if (name == null || name.isBlank()) {
            throw new DisciplineDeletionFailed("[Discipline Service] Discipline name is invalid");
        }

        var disciplines = disciplineRepository.readAll().stream().filter(d -> d.getName().equals(name)).toList();

        try {
            disciplineRepository.deleteMany(disciplines);
        } catch (Exception e) {
            throw new DisciplineDeletionFailed(" [DisciplineService] Failed to delete disciplines.", e);
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

        if (disciplineId < 0) {
            throw new DisciplineNotFoundException("[Discipline Service] Discipline id is invalid");
        }

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

    @Override
    public Discipline addTeacherToDiscipline(String teacherName, String disciplineName) throws TeacherNotFoundException, DisciplineNotFoundException {

        if (teacherName == null || teacherName.isBlank()) {
            throw new TeacherNotFoundException("[Discipline Service] Teacher name is invalid");
        }

        if (disciplineName == null || disciplineName.isBlank()) {
            throw new DisciplineNotFoundException("[Discipline Service] Discipline name is invalid");
        }

        var teachers = this.teacherRepository.readAll().stream().filter(t -> t.getName().equals(teacherName)).toList();

        if (teachers.isEmpty()) {
            throw new TeacherNotFoundException("[DisciplineService] Teacher not found!");
        }
        var teacher = teachers.get(0);

        var disciplines = this.disciplineRepository.readAll().stream().filter(d -> d.getName().equals(disciplineName)).toList();
        if (disciplines.isEmpty()) {
            throw new DisciplineNotFoundException("[DisciplineService] Discipline not found!");
        }
        var discipline = disciplines.get(0);

        var disciplineTeachers = discipline.getTeachers();
        disciplineTeachers.add(teacher);
        discipline.setTeachers(disciplineTeachers);

        try {
            disciplineRepository.save(discipline);
            teacherRepository.save(teacher);
        }
        catch (RepositoryOperationException e) {
            System.out.println(e.getMessage());
        }

        return discipline;
    }
}
