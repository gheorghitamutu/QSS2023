package org.dataaccess.discipline;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Discipline;

/**
 * This is the interface for Discipline.
 */
public interface IDisciplineRepository extends IRepository<Discipline> {

    /**
     * This is the method to get a discipline by name.
     * @param name The name.
     * @return The discipline.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Discipline getByName(String name) throws RepositoryOperationException, ValidationException;

    /**
     * This is the method to create a new discipline.
     * @param name The name.
     * @param credits The credits.
     * @return The discipline.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException, ValidationException;
}
