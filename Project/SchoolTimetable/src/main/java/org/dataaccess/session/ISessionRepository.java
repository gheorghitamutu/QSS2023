package org.dataaccess.session;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Session;

public interface ISessionRepository extends IRepository<Session> {
    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException;
}
