package org.dataaccess.session;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Session;

/**
 * An interface representing a repository for managing Session entities.
 * Extends the IRepository interface with Session as the generic type.
 * This interface defines the repository operations that can be performed, specific to session repository.
 */
public interface ISessionRepository extends IRepository<Session> {

    /**
     * Creates a new Session entity with the specified type and half year.
     *
     * @param type     The type of the new Session.
     * @param halfYear The half year of the new Session.
     * @return The newly created Session entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Session attributes fails.
     */
    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException, ValidationException;
}
