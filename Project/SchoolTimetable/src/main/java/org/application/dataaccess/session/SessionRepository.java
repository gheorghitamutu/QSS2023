package org.application.dataaccess.session;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;
import org.application.domain.models.Session;

import java.util.Date;

public class SessionRepository extends BaseRepository<Session> implements ISessionRepository {

    @Inject
    public SessionRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException {
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
