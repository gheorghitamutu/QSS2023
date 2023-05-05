package org.application.dataaccess.session;

import org.application.dataaccess.repository.IRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Session;

public interface ISessionRepository extends IRepository<Session> {
    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException;
}
