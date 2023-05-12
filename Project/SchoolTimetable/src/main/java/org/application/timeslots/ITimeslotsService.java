package org.application.timeslots;

import org.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.session.SessionNotFoundException;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public interface ITimeslotsService {

    public Timeslot addTimeslot(Date startDate, Date endDate, Date time,
                                Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                Room room, Session session) throws TimeslotAdditionException;

    public Timeslot addTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day,
                                Timeslot.Periodicity periodicity, String roomName, String disciplineName) throws TimeslotAdditionException, SessionNotFoundException, DisciplineNotFoundException, RoomNotFoundException;

    public boolean deleteTimeslot(int timeslotId) throws TimeslotNotFoundException, TimeslotDeletionFailed;

    public boolean deleteTimeslot(Date startDate, Date time, Duration duration, String roomName) throws TimeslotNotFoundException, TimeslotDeletionFailed, RoomNotFoundException;

    public boolean deleteAll() throws TimeslotDeletionFailed;

    public Timeslot getTimeslotById(int timeslotId) throws TimeslotNotFoundException;

    public List<Timeslot> getTimeslots();

    public List<Timeslot> getSortedTimeslotsByStartDateAndTime();

    public List<Timeslot> getSortedTimeslotsByStartTime();
}
