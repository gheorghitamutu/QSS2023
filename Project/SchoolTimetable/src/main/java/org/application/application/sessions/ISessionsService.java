package org.application.application.sessions;

import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.room.RoomAdditionException;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.room.RoomNotFoundException;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.exceptions.student.StudentNotFoundException;
import org.application.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.Room;
import org.application.domain.models.Session;

import java.util.List;

public interface ISessionsService {

    public Session addSession(Session.Type type, String halfYear, String disciplineName) throws SessionAdditionException, DisciplineNotFoundException;

    public boolean deleteSession(int sessionId) throws SessionNotFoundException, SessionDeletionFailed;

    public boolean deleteSession(String disciplineName) throws DisciplineNotFoundException, SessionNotFoundException, SessionDeletionFailed;

    public boolean deleteAll() throws SessionDeletionFailed;

    public Session getSessionById(int SessionId) throws SessionNotFoundException;

    public List<Session> getSessions();

    public List<Session> getSessionsByHalfYear(String hf);

    public Session addTeacherToSession(String disciplineName, String teacherName) throws DisciplineNotFoundException, SessionNotFoundException, TeacherNotFoundException;

    public Session addTeacherToSession(int sessionId, String teacherName) throws SessionNotFoundException, TeacherNotFoundException;

    public Session addGroupToSession(String disciplineName, String groupName) throws StudentGroupNotFoundException, DisciplineNotFoundException;

    public Session addGroupToSession(int sessionId, String groupName) throws SessionNotFoundException, StudentGroupNotFoundException;
}
