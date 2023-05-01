package org.application.application.sessions;

import org.application.domain.exceptions.room.RoomAdditionException;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.room.RoomNotFoundException;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.models.Room;
import org.application.domain.models.Session;

import java.util.List;

public interface ISessionsService {

    public Session addSession(Session.Type type, String halfYear) throws SessionAdditionException;

    public boolean deleteSession(int sessionId) throws SessionNotFoundException, SessionDeletionFailed;

    public Session getSessionById(int SessionId) throws SessionNotFoundException;

    public List<Session> getSessions();

    public List<Session> getSessionsByHalfYear(String hf);
}
