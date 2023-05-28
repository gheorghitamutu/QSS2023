package org.application.disciplines;

import org.domain.exceptions.discipline.DisciplineAdditionException;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Discipline;

import java.util.List;

/**
 * Represents a service interface for managing Discipline entities.
 * This interface defines the service operations that can be performed, specific to discipline service.
 */
public interface IDisciplinesService {

    /**
     * Adds a new Discipline with the given name and credits.
     * @param name The name of the new Discipline.
     * @param credits The number of credits associated with the new Discipline.
     * @return The newly created Discipline.
     * @throws DisciplineAdditionException If the Discipline could not be added.
     * @throws ValidationException If the provided name or credits are invalid.
     */
    public Discipline addDiscipline(String name, int credits) throws DisciplineAdditionException, ValidationException;

    /**
     * Deletes a Discipline with the given id.
     * @param disciplineId The id of the Discipline to delete.
     * @return True if the Discipline was deleted, false otherwise.
     * @throws DisciplineNotFoundException If the Discipline with the given id was not found.
     * @throws DisciplineDeletionFailed If the Discipline could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    public boolean deleteDiscipline(int disciplineId) throws DisciplineNotFoundException, DisciplineDeletionFailed, ValidationException;

    /**
     * Deletes a Discipline with the given name.
     * @param name The name of the Discipline to delete.
     * @return True if the Discipline was deleted, false otherwise.
     * @throws DisciplineDeletionFailed If the Discipline could not be deleted.
     */
    public boolean deleteDisciplines(String name) throws DisciplineDeletionFailed;

    /**
     * Deletes all Discipline entities.
     * @return True if all Discipline entities were deleted, false otherwise.
     * @throws DisciplineDeletionFailed If the Discipline entities could not be deleted.
     */
    public boolean deleteAll() throws DisciplineDeletionFailed;

    /**
     * Retrieves a Discipline by its ID.
     * @param disciplineId The ID of the Discipline to retrieve.
     * @return The Discipline with the specified ID.
     * @throws DisciplineNotFoundException If the Discipline with the given ID was not found.
     */
    public Discipline getDisciplineById(int disciplineId) throws DisciplineNotFoundException;

    /**
     * Retrieves all Discipline entities.
     * @return A list of all Discipline entities.
     */
    public List<Discipline> getDisciplines();

    /**
     * Adds a teacher to a discipline.
     * @param teacherName The name of the teacher to add.
     * @param disciplineName The name of the discipline to add the teacher to.
     * @return The discipline with the added teacher.
     * @throws TeacherNotFoundException If the teacher with the given name was not found.
     * @throws DisciplineNotFoundException If the discipline with the given name was not found.
     */
    public Discipline addTeacherToDiscipline(String teacherName, String disciplineName) throws TeacherNotFoundException, DisciplineNotFoundException;
}
