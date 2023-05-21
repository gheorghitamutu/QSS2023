package org.application.timeslots;

import com.google.inject.Inject;
import jakarta.validation.Valid;
import org.dataaccess.discipline.IDisciplineRepository;
import org.dataaccess.room.IRoomRepository;
import org.dataaccess.timeslot.ITimeslotRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.session.SessionNotFoundException;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

        validateTimeslotRelatedParams(startDate, endDate, time, duration, day, periodicity, room, session);

        Timeslot timeslot;

        try {
            timeslot = timeslotRepository.createNewTimeslot(startDate, endDate, time, duration, day, periodicity, room, session);
        } catch (RepositoryOperationException e) {
            throw new TimeslotAdditionException("[TimeslotService] Failed adding timeslot!", e);
        }

        return timeslot;
    }

    @Override
    public Timeslot addTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, String roomName, String disciplineName) throws TimeslotAdditionException, SessionNotFoundException, DisciplineNotFoundException, RoomNotFoundException {

        validateTimeslotRelatedParams(startDate, endDate, time, duration, day, periodicity, roomName, disciplineName);

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

        if (timeslotId < 0) {
            throw new IllegalArgumentException("[TimeslotService] Timeslot id cannot be negative!");
        }

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

        if (timeslotId < 0) {
            throw new IllegalArgumentException("[TimeslotService] Timeslot id cannot be negative!");
        }

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

    @Override
    public List<Timeslot> getSortedTimeslotsByStartTime() {
        return getTimeslots()
                .stream()
                .sorted(
                        Comparator
                                .comparing(Timeslot::getTime))
                .toList();
    }

    private void validateTimeslotRelatedParams(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                               @Valid Room room, @Valid Session session) {
        if (startDate == null) {
            throw new IllegalArgumentException("[TimeslotRepository Validation] Start date cannot be null.");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] End date cannot be null.");
        }

        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be after end date.");
        }

        if (startDate.equals(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be equal to end date.");
        }

        if (time == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Time cannot be null.");
        }

        if (duration == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Duration cannot be null.");
        }

        if (duration.isNegative()) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Duration cannot be negative.");
        }

        if (day == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Day cannot be null.");
        }

        if (periodicity == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Periodicity cannot be null.");
        }

        if (session == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Session cannot be null.");
        }
    }


    private void validateTimeslotRelatedParams(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                               String room, String disciplineName) {
        if (startDate == null) {
            throw new IllegalArgumentException("[TimeslotRepository Validation] Start date cannot be null.");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] End date cannot be null.");
        }

        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be after end date.");
        }

        if (startDate.equals(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be equal to end date.");
        }

        if (time == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Time cannot be null.");
        }

        if (duration == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Duration cannot be null.");
        }

        if (duration.isNegative()) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Duration cannot be negative.");
        }

        if (day == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Day cannot be null.");
        }

        if (periodicity == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Periodicity cannot be null.");
        }

        if (room == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Room cannot be null.");
        }

        if (disciplineName == null) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Discipline name cannot be null.");
        }
    }
}
