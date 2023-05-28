package org.dataaccess.session;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Session;

import java.util.Date;

/**
 * A repository class for managing Session entities within the database.
 * Extends the BaseRepository class with Session as the generic type and implements the ISessionRepository interface.
 * Contains methods for creating new Session entities.
 */
public class SessionRepository extends BaseRepository<Session> implements ISessionRepository {

    /**
     * Constructs a new SessionRepository with the specified Hibernate provider.
     *
     * @param hibernateProvider The Hibernate provider to be used for database operations.
     */
    @Inject
    public SessionRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    /**
     * Creates a new Session object with the specified type and half year.
     *
     * @param type     The type of the new Session.
     * @param halfYear The half year of the new Session.
     * @return The newly created Session entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Session attributes fails.
     */
    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotBlank(halfYear, IllegalArgumentException.class, "Half year cannot be blank.", null);
        ValidationHelpers.requireNotNull(type, IllegalArgumentException.class, "Type cannot be null.", null);

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
