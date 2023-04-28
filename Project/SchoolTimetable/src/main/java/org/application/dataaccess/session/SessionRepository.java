package org.application.dataaccess.session;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.models.Session;

public class SessionRepository extends BaseRepository<Session> implements ISessionRepository {

    @Inject
    public SessionRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
