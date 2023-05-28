package org.application.sessions;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.discipline.IDisciplineRepository;
import org.dataaccess.session.ISessionRepository;
import org.dataaccess.studentgroup.IStudentGroupRepository;
import org.dataaccess.teacher.ITeacherRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.session.SessionAdditionException;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.session.SessionNotFoundException;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Session;

import java.text.MessageFormat;
import java.util.List;

/**
 * Represents a service class for managing Session entities.
 * This class defines the service operations that can be performed, specific to session service.
 */
public class SessionsService implements ISessionsService {

    /**
     * The session repository.
     */
    private final ISessionRepository sessionRepository;

    /**
     * The discipline repository.
     */
    private final IDisciplineRepository disciplineRepository;

    /**
     * The teacher repository.
     */
    private final ITeacherRepository teacherRepository;

    /**
     * The student group repository.
     */
    private final IStudentGroupRepository studentGroupRepository;

    /**
     * Initializes a new instance of the {@link SessionsService} class.
     * @param sessionRepository The session repository.
     * @param disciplineRepository The discipline repository.
     * @param teacherRepository The teacher repository.
     * @param studentGroupRepository The student group repository.
     */
    @Inject
    public SessionsService(ISessionRepository sessionRepository, IDisciplineRepository disciplineRepository, ITeacherRepository teacherRepository, IStudentGroupRepository studentGroupRepository) {
        this.sessionRepository = sessionRepository;
        this.disciplineRepository = disciplineRepository;
        this.teacherRepository = teacherRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    /**
     * Adds a new Session with the given type, half year and discipline name.
     * @param type The type of the new Session.
     * @param halfYear The half year of the new Session.
     * @param disciplineName The discipline name of the new Session.
     * @return The newly created Session.
     * @throws SessionAdditionException If the Session could not be added.
     * @throws DisciplineNotFoundException If the discipline could not be found.
     * @throws ValidationException If the provided type, half year or discipline name are invalid.
     */
    @Override
    public Session addSession(Session.Type type, String halfYear, String disciplineName) throws SessionAdditionException, DisciplineNotFoundException, ValidationException {

        ValidationHelpers.requireNotNull(type, IllegalArgumentException.class, "[RoomService] Room type is invalid", null);
        ValidationHelpers.requireNotBlank(halfYear, IllegalArgumentException.class, "[Session Service] Session half year is invalid", null);
        ValidationHelpers.requireNotBlank(disciplineName, IllegalArgumentException.class, "[Session Service] Session discipline name is invalid", null);

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
        } catch (RepositoryOperationException | ValidationException e) {
            throw new SessionAdditionException("[SessionService] Failed adding session!", e);
        }

        return session;
    }

    /**
     * Deletes the Session with the given id.
     * @param sessionId The id of the Session to delete.
     * @return True if the Session was deleted, false otherwise.
     * @throws SessionNotFoundException If the Session could not be found.
     * @throws SessionDeletionFailed If the Session could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    @Override
    public boolean deleteSession(int sessionId) throws SessionNotFoundException, SessionDeletionFailed, ValidationException {

        ValidationHelpers.requirePositiveOrZero(sessionId, IllegalArgumentException.class, "[SessionService] Session id is invalid", null);

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

    /**
     * Deletes the Session with the given discipline name.
     * @param disciplineName The discipline name of the Session to delete.
     * @return True if the Session was deleted, false otherwise.
     * @throws DisciplineNotFoundException If the discipline could not be found.
     * @throws SessionNotFoundException If the Session could not be found.
     * @throws SessionDeletionFailed If the Session could not be deleted.
     */
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

    /**
     * Deletes all Sessions.
     * @return True if the Sessions were deleted, false otherwise.
     * @throws SessionDeletionFailed If the Sessions could not be deleted.
     */
    public boolean deleteAll() throws SessionDeletionFailed {
        try {
            sessionRepository.deleteMany(sessionRepository.readAll());
        } catch (Exception e) {
            throw new SessionDeletionFailed(" [SessionService] Failed to delete sessions.", e);
        }

        return true;
    }

    /**
     * Gets the Session with the given id.
     * @param sessionId The id of the Session to get.
     * @return The Session with the given id.
     * @throws SessionNotFoundException If the Session could not be found.
     * @throws ValidationException If the provided id is invalid.
     */
    @Override
    public Session getSessionById(int sessionId) throws SessionNotFoundException, ValidationException {

        ValidationHelpers.requirePositiveOrZero(sessionId, IllegalArgumentException.class, "[SessionService] Session id is invalid", null);

        var session = sessionRepository.getById(sessionId);
        if (session == null) {
            throw new SessionNotFoundException(MessageFormat.format("[SessionService] Session with id {0} not found.", sessionId));
        }
        return session;
    }

    /**
     * Gets all Sessions.
     * @return A list of all Sessions.
     */
    @Override
    public List<Session> getSessions() {
        return sessionRepository.readAll();
    }

    /**
     * Retrieves all Sessions with the given half year.
     * @param hf The half year of the Session entities to retrieve.
     * @return A list of Sessions with the given half year.
     * @throws ValidationException If the provided half year is invalid.
     */
    @Override
    public List<Session> getSessionsByHalfYear(String hf) throws ValidationException {

        ValidationHelpers.requireNotBlank(hf, IllegalArgumentException.class, "[Session Service] Session half year is invalid", null);

        return sessionRepository.readAll().stream().filter(session -> session.getHalfYear().equals(hf)).toList();
    }

    /**
     * Adds a teacher to the Session with the given id.
     * @param disciplineName The discipline name of the session.
     * @param teacherName The name of the teacher.
     * @return The Session with the given id.
     * @throws DisciplineNotFoundException If the discipline could not be found.
     * @throws SessionNotFoundException If the Session could not be found.
     * @throws TeacherNotFoundException If the teacher could not be found.
     */
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

    /**
     * Adds a teacher to the Session with the given id.
     * @param sessionId The id of the Session.
     * @param teacherName The name of the teacher.
     * @return The Session with the given id.
     * @throws SessionNotFoundException If the Session could not be found.
     * @throws TeacherNotFoundException If the teacher could not be found.
     */
    @Override
    public Session addTeacherToSession(int sessionId, String teacherName) throws SessionNotFoundException, TeacherNotFoundException {

        if (sessionId < 0) {
            throw new SessionNotFoundException("[Session Service] Session id is invalid");
        }

        if (teacherName == null || teacherName.isBlank()) {
            throw new SessionNotFoundException("[Session Service] Teacher name is invalid");
        }

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

    /**
     *
     * @param disciplineName The discipline name of the session.
     * @param groupName The name of the group.
     * @return The Session with the given id.
     * @throws StudentGroupNotFoundException If the student group could not be found.
     * @throws DisciplineNotFoundException If the discipline could not be found.
     */
    @Override
    public Session addGroupToSession(String disciplineName, String groupName) throws StudentGroupNotFoundException, DisciplineNotFoundException {

        if (groupName == null || groupName.isBlank()) {
            throw new StudentGroupNotFoundException("[Session Service] Group name is invalid");
        }

        if (disciplineName == null || disciplineName.isBlank()) {
            throw new DisciplineNotFoundException("[Session Service] Discipline name is invalid");
        }

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

        if (sessionId < 0) {
            throw new IllegalArgumentException("[Session Service] Session id is invalid");
        }

        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("[Session Service] Group name is invalid");
        }
        
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
