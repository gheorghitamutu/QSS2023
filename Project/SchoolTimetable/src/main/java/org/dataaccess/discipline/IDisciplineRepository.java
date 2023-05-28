package org.dataaccess.discipline;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Discipline;


/**
 * Represents a repository interface for managing Discipline entities.
 * Extends the IRepository interface with Discipline as the generic type.
 * This interface defines the repository operations that can be performed, specific to discipline repository.
 */
public interface IDisciplineRepository extends IRepository<Discipline> {

    /**
     * Retrieves a Discipline by its name.
     *
     * @param name The name of the Discipline to retrieve.
     * @return The Discipline with the specified name.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException          If the provided name is invalid.
     */
    public Discipline getByName(String name) throws RepositoryOperationException, ValidationException;

    /**
     * Creates a new Discipline with the given name and credits.
     *
     * @param name    The name of the new Discipline.
     * @param credits The number of credits associated with the new Discipline.
     * @return The newly created Discipline.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException          If the provided name or credits are invalid.
     */
    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException, ValidationException;
}
