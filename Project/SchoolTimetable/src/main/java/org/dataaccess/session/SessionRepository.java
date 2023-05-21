package org.dataaccess.session;

import com.google.inject.Inject;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Session;

import java.util.Date;

public class SessionRepository extends BaseRepository<Session> implements ISessionRepository {

    @Inject
    public SessionRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException {

        if (type == null) {
            throw new RepositoryOperationException("[SessionRepository Validation] Type cannot be null.");
        }

        if (halfYear == null) {
            throw new RepositoryOperationException("[SessionRepository Validation] Half year cannot be null.");
        }

        var session = new Session();
        session.setType(type);
        session.setHalfYear(halfYear);
        session.setInsertTime(new Date());

        try {
            save(session);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryOperationException("SessionRepository] Couldn't create new session.", e);
        }

        return session;
    }
}
