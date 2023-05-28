package org.dataaccess.session;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Session;

/**
 * This is the interface for Session.
 */
public interface ISessionRepository extends IRepository<Session> {

    /**
     * This is the method to create a new session.
     * @param type The type.
     * @param halfYear The half year.
     * @return The session.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException, ValidationException;
}
