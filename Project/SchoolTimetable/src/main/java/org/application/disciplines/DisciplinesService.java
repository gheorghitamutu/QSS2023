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

/**
 * Represents a service class for managing Discipline entities.
 * This class defines the service operations that can be performed, specific to discipline service.
 */
public class DisciplinesService implements IDisciplinesService {

    /**
     * The repository used for accessing Discipline entities.
     */
    private final IDisciplineRepository disciplineRepository;

    /**
     * The repository used for accessing Teacher entities.
     */
    private final ITeacherRepository teacherRepository;

    /**
     * Creates a new instance of the {@link DisciplinesService} class.
     * @param disciplineRepository The repository used for accessing Discipline entities.
     * @param teacherRepository The repository used for accessing Teacher entities.
     */
    @Inject
    public DisciplinesService(IDisciplineRepository disciplineRepository, ITeacherRepository teacherRepository) {
        this.disciplineRepository = disciplineRepository;
        this.teacherRepository = teacherRepository;
    }

    /**
     * Adds a new Discipline with the given name and credits.
     * @param name The name of the new Discipline.
     * @param credits The number of credits associated with the new Discipline.
     * @return The newly created Discipline.
     * @throws DisciplineAdditionException If the Discipline could not be added.
     * @throws ValidationException If the provided name or credits are invalid.
     */
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

    /**
     * Deletes a Discipline with the specified id.
     * @param disciplineId The id of the Discipline to delete.
     * @return The Discipline with the specified id.
     * @throws DisciplineNotFoundException If the Discipline could not be found.
     * @throws DisciplineDeletionFailed If the Discipline could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
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

    /**
     * Deletes a Discipline with the specified name.
     * @param name The name of the Discipline to delete.
     * @return The Discipline with the specified name.
     * @throws DisciplineDeletionFailed If the Discipline could not be deleted.
     */
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

    /**
     * Deletes all Discipline entities.
     * @return True if the operation was successful, false otherwise.
     * @throws DisciplineDeletionFailed If the Discipline could not be deleted.
     */
    @Override
    public boolean deleteAll() throws DisciplineDeletionFailed {
        try {
            disciplineRepository.deleteMany(disciplineRepository.readAll());
        } catch (Exception e) {
            throw new DisciplineDeletionFailed(" [DisciplineService] Failed to delete disciplines.", e);
        }

        return true;
    }

    /**
     * Gets a Discipline with the specified id.
     * @param disciplineId The id of the Discipline to get.
     * @return The Discipline with the specified id.
     * @throws DisciplineNotFoundException If the Discipline could not be found.
     */
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

    /**
     * Retrieves all Discipline entities.
     * @return A list of all Discipline entities.
     */
    @Override
    public List<Discipline> getDisciplines() {
        return disciplineRepository.readAll();
    }

    /**
     * Adds a new Teacher to a Discipline.
     * @param teacherName The name of the Teacher to add.
     * @param disciplineName The name of the Discipline to add the Teacher to.
     * @return The Discipline with the newly added Teacher.
     * @throws TeacherNotFoundException If the Teacher could not be found.
     * @throws DisciplineNotFoundException If the Discipline could not be found.
     */
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
