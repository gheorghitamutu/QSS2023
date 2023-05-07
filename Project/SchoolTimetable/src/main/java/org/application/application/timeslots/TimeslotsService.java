package org.application.application.timeslots;

import com.google.inject.Inject;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.room.IRoomRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.timeslot.ITimeslotRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.room.RoomNotFoundException;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.models.Room;
import org.application.domain.models.Session;
import org.application.domain.models.Timeslot;

import java.sql.Time;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class TimeslotsService implements ITimeslotsService {

    private final ITimeslotRepository timeslotRepository;
    private final IRoomRepository roomRepository;
    private final IDisciplineRepository disciplineRepository;

    @Inject
    public TimeslotsService(ITimeslotRepository timeslotRepository, IRoomRepository roomRepository, IDisciplineRepository disciplineRepository) {
        this.timeslotRepository = timeslotRepository;
        this.roomRepository = roomRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @Override
    public Timeslot addTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, Room room, Session session) throws TimeslotAdditionException {
        Timeslot timeslot;

        try {
            timeslot = timeslotRepository.createNewTimeslot(startDate, endDate, time, duration, day, periodicity, room, session);
        } catch (RepositoryOperationException e) {
            throw new RuntimeException(e);
        }


        try {
            timeslotRepository.save(timeslot);
        } catch (Exception e) {
            throw new TimeslotAdditionException("[TimeslotService] Failed adding timeslot!", e);
        }

        return timeslot;
    }

    @Override
    public Timeslot addTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, String roomName, String disciplineName) throws TimeslotAdditionException, SessionNotFoundException, DisciplineNotFoundException, RoomNotFoundException {
        var rooms = roomRepository.readAll().stream().filter(room -> room.getName().equals(roomName)).toList();
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException(MessageFormat.format("[TimeslotService] Room with name {0} not found.", roomName));
        }
        var room = rooms.get(0);

        var disciplines = disciplineRepository.readAll().stream().filter(d -> d.getName().equals(disciplineName)).toList();
        if (disciplines.isEmpty()) {
            throw new DisciplineNotFoundException("[TimeslotService] Discipline not found!");
        }
        var discipline = disciplines.get(0);
        var sessions = discipline.getSessions().stream().toList();
        if (sessions.isEmpty()) {
            throw new SessionNotFoundException("[TimeslotService] Session not found!");
        }
        var session = sessions.get(0);

        return addTimeslot(startDate, endDate, time, duration, day, periodicity, room, session);
    }

    @Override
    public boolean deleteTimeslot(int timeslotId) throws TimeslotNotFoundException, TimeslotDeletionFailed {
        var timeslot = timeslotRepository.getById(timeslotId);
        if (timeslot == null) {
            throw new TimeslotNotFoundException(MessageFormat.format("[TimeslotService] Timeslot with id {0} not found.", timeslotId));
        }

        try {
            timeslotRepository.delete(timeslot);
        } catch (Exception e) {
            throw new TimeslotDeletionFailed(" [TimeslotService] Failed to delete timeslot.", e);
        }

        return true;
    }

    @Override
    public boolean deleteTimeslot(Date startDate, Date time, Duration duration, String roomName) throws TimeslotNotFoundException, TimeslotDeletionFailed, RoomNotFoundException {
        var rooms = roomRepository.readAll().stream().filter(room -> room.getName().equals(roomName)).toList();
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException(MessageFormat.format("[TimeslotService] Room with name {0} not found.", roomName));
        }
        var room = rooms.get(0);
        var timeslots = timeslotRepository.readAll().stream()
                .filter(t -> t.getRoom().equals(room))
                .filter(t -> t.getStartDate().equals(startDate))
                .filter(t -> t.getTime().equals(time))
                .filter(t -> t.getTimespan().equals(duration)).toList();
        if (timeslots.isEmpty()) {
            throw new TimeslotNotFoundException(MessageFormat.format("[TimeslotService] Timeslot with not found for room name {0}.", roomName));
        }

        var timeslot = timeslots.get(0);
        try {
            timeslotRepository.delete(timeslot);
        } catch (Exception e) {
            throw new TimeslotDeletionFailed(" [TimeslotService] Failed to delete timeslot.", e);
        }

        return true;
    }

    @Override
    public boolean deleteAll() throws TimeslotDeletionFailed {
        try {
            timeslotRepository.deleteMany(timeslotRepository.readAll());
        } catch (Exception e) {
            throw new TimeslotDeletionFailed(" [TimeslotService] Failed to delete timeslots.", e);
        }

        return true;
    }

    @Override
    public Timeslot getTimeslotById(int timeslotId) throws TimeslotNotFoundException {
        var timeslot = timeslotRepository.getById(timeslotId);
        if (timeslot == null) {
            throw new TimeslotNotFoundException(MessageFormat.format("[TimeslotService] Timeslot with id {0} not found.", timeslotId));
        }
        return timeslot;
    }

    @Override
    public List<Timeslot> getTimeslots() {
        return timeslotRepository.readAll();
    }

    @Override
    public List<Timeslot> getSortedTimeslotsByStartDateAndTime() {
        return getTimeslots()
                .stream()
                .sorted(
                        Comparator
                                .comparing(Timeslot::getStartDate)
                                .thenComparing(Timeslot::getTime))
                .toList();
    }
}
