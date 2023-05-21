package org.application.sessions;

import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.session.SessionAdditionException;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.session.SessionNotFoundException;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.models.Session;

import java.util.List;

public interface ISessionsService {

    public Session addSession(Session.Type type, String halfYear, String disciplineName) throws SessionAdditionException, DisciplineNotFoundException;

    public boolean deleteSession(int sessionId) throws SessionNotFoundException, SessionDeletionFailed;

    public boolean deleteSession(String disciplineName) throws DisciplineNotFoundException, SessionNotFoundException, SessionDeletionFailed;

    public boolean deleteAll() throws SessionDeletionFailed;

    public Session getSessionById(int SessionId) throws SessionNotFoundException;

    public List<Session> getSessions();

    public List<Session> getSessionsByHalfYear(String hf) throws SessionNotFoundException;

    public Session addTeacherToSession(String disciplineName, String teacherName) throws DisciplineNotFoundException, SessionNotFoundException, TeacherNotFoundException;

    public Session addTeacherToSession(int sessionId, String teacherName) throws SessionNotFoundException, TeacherNotFoundException;

    public Session addGroupToSession(String disciplineName, String groupName) throws StudentGroupNotFoundException, DisciplineNotFoundException, SessionNotFoundException;

    public Session addGroupToSession(int sessionId, String groupName) throws SessionNotFoundException, StudentGroupNotFoundException;
}
