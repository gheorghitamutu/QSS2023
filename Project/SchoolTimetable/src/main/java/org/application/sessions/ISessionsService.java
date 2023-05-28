package org.application.sessions;

import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.session.SessionAdditionException;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.session.SessionNotFoundException;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Session;

import java.util.List;

/**
 * Represents a service interface for managing Session entities.
 * This interface defines the service operations that can be performed, specific to session service.
 */
public interface ISessionsService {

    /**
     * Adds a new Session with the given type, half year and discipline name.
     * @param type The type of the new Session.
     * @param halfYear The half year of the new Session.
     * @param disciplineName The discipline name of the new Session.
     * @return The newly created Session.
     * @throws SessionAdditionException If the Session could not be added.
     * @throws DisciplineNotFoundException If the Discipline with the given name was not found.
     * @throws ValidationException If the provided type, half year or discipline name are invalid.
     */
    public Session addSession(Session.Type type, String halfYear, String disciplineName) throws SessionAdditionException, DisciplineNotFoundException, ValidationException;

    /**
     * Deletes a Session with the given id.
     * @param sessionId The id of the Session to delete.
     * @return True if the Session was deleted, false otherwise.
     * @throws SessionNotFoundException If the Session with the given id was not found.
     * @throws SessionDeletionFailed If the Session could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    public boolean deleteSession(int sessionId) throws SessionNotFoundException, SessionDeletionFailed, ValidationException;

    /**
     * Deletes a Session with the given discipline name.
     * @param disciplineName The discipline name of the Session to delete.
     * @return True if the Session was deleted, false otherwise.
     * @throws DisciplineNotFoundException If the Discipline with the given name was not found.
     * @throws SessionNotFoundException If the Session with the given discipline name was not found.
     * @throws SessionDeletionFailed If the Session could not be deleted.
     */
    public boolean deleteSession(String disciplineName) throws DisciplineNotFoundException, SessionNotFoundException, SessionDeletionFailed;

    /**
     * Deletes all Session entities.
     * @return True if all Session entities were deleted, false otherwise.
     * @throws SessionDeletionFailed If the Session entities could not be deleted.
     */
    public boolean deleteAll() throws SessionDeletionFailed;

    /**
     * Retrieves a Session by its ID.
     * @param SessionId The ID of the Session to retrieve.
     * @return The Session with the specified ID.
     * @throws SessionNotFoundException If the Session with the given ID was not found.
     * @throws ValidationException If the provided id is invalid.
     */
    public Session getSessionById(int SessionId) throws SessionNotFoundException, ValidationException;

    /**
     * Retrieves all Session entities.
     * @return A list of all Session entities.
     */
    public List<Session> getSessions();

    /**
     * Retrieves all Session entities with the given half year.
     * @param hf The half year of the Session entities to retrieve.
     * @return A list of all Session entities with the given half year.
     * @throws SessionNotFoundException If the Session with the given half year was not found.
     * @throws ValidationException If the provided half year is invalid.
     */
    public List<Session> getSessionsByHalfYear(String hf) throws SessionNotFoundException, ValidationException;

    /**
     * Adds a teacher to a session.
     * @param disciplineName The discipline name of the session.
     * @param teacherName The name of the teacher.
     * @return The session with the added teacher.
     * @throws DisciplineNotFoundException If the discipline was not found.
     * @throws SessionNotFoundException If the session was not found.
     * @throws TeacherNotFoundException If the teacher was not found.
     */
    public Session addTeacherToSession(String disciplineName, String teacherName) throws DisciplineNotFoundException, SessionNotFoundException, TeacherNotFoundException;

    /**
     * Adds a teacher to a session.
     * @param sessionId The id of the session.
     * @param teacherName The name of the teacher.
     * @return The session with the added teacher.
     * @throws SessionNotFoundException If the session was not found.
     * @throws TeacherNotFoundException If the teacher was not found.
     */
    public Session addTeacherToSession(int sessionId, String teacherName) throws SessionNotFoundException, TeacherNotFoundException;

    /**
     * Adds a group to a session.
     * @param disciplineName The discipline name of the session.
     * @param groupName The name of the group.
     * @return The session with the added group.
     * @throws StudentGroupNotFoundException If the group was not found.
     * @throws DisciplineNotFoundException If the discipline was not found.
     * @throws SessionNotFoundException If the session was not found.
     */
    public Session addGroupToSession(String disciplineName, String groupName) throws StudentGroupNotFoundException, DisciplineNotFoundException, SessionNotFoundException;

    /**
     * Adds a group to a session.
     * @param sessionId The id of the session.
     * @param groupName The name of the group.
     * @return The session with the added group.
     * @throws SessionNotFoundException If the session was not found.
     * @throws StudentGroupNotFoundException If the group was not found.
     */
    public Session addGroupToSession(int sessionId, String groupName) throws SessionNotFoundException, StudentGroupNotFoundException;
}
