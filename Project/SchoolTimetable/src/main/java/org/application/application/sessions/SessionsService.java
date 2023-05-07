package org.application.application.sessions;

import com.google.inject.Inject;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.dataaccess.teacher.ITeacherRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.Session;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

public class SessionsService implements ISessionsService {

    private final ISessionRepository sessionRepository;
    private final IDisciplineRepository disciplineRepository;
    private final ITeacherRepository teacherRepository;
    private final IStudentGroupRepository studentGroupRepository;

    @Inject
    public SessionsService(ISessionRepository sessionRepository, IDisciplineRepository disciplineRepository, ITeacherRepository teacherRepository, IStudentGroupRepository studentGroupRepository) {
        this.sessionRepository = sessionRepository;
        this.disciplineRepository = disciplineRepository;
        this.teacherRepository = teacherRepository;
        this.studentGroupRepository = studentGroupRepository;
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
            sessionRepository.save(session);

            var disciplineSessions = discipline.getSessions();
            disciplineSessions.add(session);
            discipline.setSessions(disciplineSessions);
            disciplineRepository.save(discipline);
        } catch (RepositoryOperationException e) {
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

    @Override
    public Session addTeacherToSession(String disciplineName, String teacherName) throws DisciplineNotFoundException, SessionNotFoundException, TeacherNotFoundException {
        var disciplines = disciplineRepository.readAll().stream().filter(d -> d.getName().equals(disciplineName)).toList();
        if (disciplines.isEmpty()) {
            throw new DisciplineNotFoundException("[SessionService] Discipline not found!");
        }
        var discipline = disciplines.get(0);

        var teachers = teacherRepository.readAll().stream().filter(d -> d.getName().equals(teacherName)).toList();
        if (teachers.isEmpty()) {
            throw new TeacherNotFoundException("[SessionService] Teacher not found!");
        }
        var teacher = teachers.get(0);

        var sessions = discipline.getSessions();
        if (sessions.isEmpty()) {
            throw new SessionNotFoundException("[SessionService] Session not found!");
        }

        var session = sessions.stream().toList().get(0);
        var sessionTeachers = session.getTeachers();
        sessionTeachers.add(teacher);
        session.setTeachers(sessionTeachers);

        try {
            sessionRepository.save(session);
        }
        catch (RepositoryOperationException e) {
            System.out.println(e.getMessage());
        }

        return session;
    }

    @Override
    public Session addTeacherToSession(int sessionId, String teacherName) throws SessionNotFoundException, TeacherNotFoundException {
        var session = this.sessionRepository.getById(sessionId);
        if (session == null) {
            throw new SessionNotFoundException("[SessionService] Session not found!");
        }

        var teachers = this.teacherRepository.readAll().stream().filter(d -> d.getName().equals(teacherName)).toList();
        if (teachers.isEmpty()) {
            throw new TeacherNotFoundException("[SessionService] Teacher not found!");
        }

        var teacher = teachers.get(0);
        var sessionTeachers = session.getTeachers();
        sessionTeachers.add(teacher);
        session.setTeachers(sessionTeachers);

        try {
            sessionRepository.save(session);
        }
        catch (RepositoryOperationException e) {
            System.out.println(e.getMessage());
        }

        return session;
    }

    @Override
    public Session addGroupToSession(String disciplineName, String groupName) throws StudentGroupNotFoundException, DisciplineNotFoundException, SessionNotFoundException {
        var groups = studentGroupRepository.readAll().stream().filter(d -> d.getName().equals(groupName)).toList();
        if (groups.isEmpty()) {
            throw new StudentGroupNotFoundException("[SessionService] Student group not found!");
        }
        var group = groups.get(0);

        var disciplines = disciplineRepository.readAll().stream().filter(d -> d.getName().equals(disciplineName)).toList();
        if (disciplines.isEmpty()) {
            throw new DisciplineNotFoundException("[SessionService] Discipline not found!");
        }
        var discipline = disciplines.get(0);

        if (discipline.getSessions().isEmpty()) {
            throw new DisciplineNotFoundException("[SessionService] Session not found!");
        }
        var session = discipline.getSessions().stream().toList().get(0);
        var sessionGroups = session.getGroups();
        sessionGroups.add(group);
        session.setGroups(sessionGroups);

        try {
            sessionRepository.save(session);
        }
        catch (RepositoryOperationException e) {
            System.out.println(e.getMessage());
        }

        return session;
    }

    @Override
    public Session addGroupToSession(int sessionId, String groupName) throws SessionNotFoundException, StudentGroupNotFoundException {
        var session = this.sessionRepository.getById(sessionId);
        if (session == null) {
            throw new SessionNotFoundException("[SessionService] Session not found!");
        }

        var groups = this.studentGroupRepository.readAll().stream().filter(d -> d.getName().equals(groupName)).toList();
        if (groups.isEmpty()) {
            throw new StudentGroupNotFoundException("[SessionService] Student group not found!");
        }

        var group = groups.get(0);
        var sessionGroups = session.getGroups();
        sessionGroups.add(group);
        session.setGroups(sessionGroups);

        try {
            sessionRepository.save(session);
        }
        catch (RepositoryOperationException e) {
            System.out.println(e.getMessage());
        }

        return session;
    }
}
