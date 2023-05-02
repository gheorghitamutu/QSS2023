package org.application.application.sessions;

import com.google.inject.Inject;
import org.application.dataaccess.session.ISessionRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.models.Session;

import java.text.MessageFormat;
import java.util.List;

public class SessionsService implements ISessionsService {

    private final ISessionRepository sessionRepository;

    @Inject
    public SessionsService(ISessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session addSession(Session.Type type, String halfYear) throws SessionAdditionException {
        Session session;
        try {
            session = sessionRepository.createNewSession(type, halfYear);
        } catch (RepositoryOperationException e) {
            throw new RuntimeException(e);
        }


        try {
            sessionRepository.save(session);
        } catch (Exception e) {
            throw new SessionAdditionException("[SessionService] Failed adding session!", e);
        }

        return session;
    }

    @Override
    public boolean deleteSession(int sessionId) throws SessionNotFoundException, SessionDeletionFailed {
        var session = sessionRepository.getById(sessionId);
        if (session == null) {
            throw new SessionNotFoundException(MessageFormat.format("[SessionService] Session with id {0} not found.", sessionId));
        }

        try {
            sessionRepository.delete(session);
        } catch (Exception e) {
            throw new SessionDeletionFailed(" [SessionService] Failed to delete session.", e);
        }

        return true;
    }

    @Override
    public Session getSessionById(int sessionId) throws SessionNotFoundException {
        var session = sessionRepository.getById(sessionId);
        if (session == null) {
            throw new SessionNotFoundException(MessageFormat.format("[SessionService] Session with id {0} not found.", sessionId));
        }
        return session;
    }

    @Override
    public List<Session> getSessions() {
        return sessionRepository.readAll();
    }

    @Override
    public List<Session> getSessionsByHalfYear(String hf) {
        return sessionRepository.readAll().stream().filter(session -> session.getHalfYear().equals(hf)).toList();
    }
}
