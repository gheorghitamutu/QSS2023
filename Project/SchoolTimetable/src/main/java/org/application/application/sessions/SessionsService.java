package org.application.application.sessions;

import com.google.inject.Inject;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.models.Discipline;
import org.application.domain.models.Session;

import java.text.MessageFormat;
import java.util.List;

public class SessionsService implements ISessionsService {

    private final ISessionRepository sessionRepository;
    private final IDisciplineRepository disciplineRepository;

    @Inject
    public SessionsService(ISessionRepository sessionRepository, IDisciplineRepository disciplineRepository) {
        this.sessionRepository = sessionRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @Override
    public Session addSession(Session.Type type, String halfYear, String disciplineName) throws SessionAdditionException, DisciplineNotFoundException {
        var disciplines = disciplineRepository.readAll().stream().filter(d -> d.getName().equals(disciplineName)).toList();
        if (disciplines.isEmpty()) {
            throw new DisciplineNotFoundException("[SessionService] Discipline not found!");
        }
        var discipline = disciplines.get(0);

        Session session;
        try {
            session = sessionRepository.createNewSession(type, halfYear);
            session.setDiscipline(discipline);
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
    public boolean deleteSession(String disciplineName) throws DisciplineNotFoundException, SessionNotFoundException, SessionDeletionFailed {
        var disciplines = disciplineRepository.readAll().stream().filter(d -> d.getName().equals(disciplineName)).toList();
        if (disciplines.isEmpty()) {
            throw new DisciplineNotFoundException("[SessionService] Discipline not found!");
        }
        var discipline = disciplines.get(0);
        var sessions = discipline.getSessions();
        if (sessions.isEmpty()) {
            throw new SessionNotFoundException("[SessionService] Session not found!");
        }
        var session = sessions.stream().toList().get(0);
        try {
            sessionRepository.delete(session);
        } catch (Exception e) {
            throw new SessionDeletionFailed(" [SessionService] Failed to delete session.", e);
        }

        return true;
    }

    public boolean deleteAll() throws SessionDeletionFailed {
        try {
            sessionRepository.deleteMany(sessionRepository.readAll());
        } catch (Exception e) {
            throw new SessionDeletionFailed(" [SessionService] Failed to delete sessions.", e);
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
