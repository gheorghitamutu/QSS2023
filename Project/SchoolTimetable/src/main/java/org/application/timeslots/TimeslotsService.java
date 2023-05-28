package org.application.timeslots;

import com.google.inject.Inject;
import jakarta.validation.Valid;
import org.application.helpers.ValidationHelpers;
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
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * This class is the service for the timeslots.
 */
public class TimeslotsService implements ITimeslotsService {

    /**
     * The timeslot repository.
     */
    private final ITimeslotRepository timeslotRepository;

    /**
     * The room repository.
     */
    private final IRoomRepository roomRepository;

    /**
     * The discipline repository.
     */
    private final IDisciplineRepository disciplineRepository;

    /**
     * Initializes a new instance of the {@link TimeslotsService} class.
     * @param timeslotRepository The timeslot repository.
     * @param roomRepository The room repository.
     * @param disciplineRepository The discipline repository.
     */
    @Inject
    public TimeslotsService(ITimeslotRepository timeslotRepository, IRoomRepository roomRepository, IDisciplineRepository disciplineRepository) {
        this.timeslotRepository = timeslotRepository;
        this.roomRepository = roomRepository;
        this.disciplineRepository = disciplineRepository;
    }

    /**
     * Adds a new Timeslot with the given start date, end date, time, duration, day, periodicity, room and session.
     * @param startDate The start date of the new Timeslot.
     * @param endDate The end date of the new Timeslot.
     * @param time The time of the new Timeslot.
     * @param duration The duration of the new Timeslot.
     * @param day The day of the new Timeslot.
     * @param periodicity The periodicity of the new Timeslot.
     * @param room The room of the new Timeslot.
     * @param session The session of the new Timeslot.
     * @return The newly created Timeslot.
     * @throws TimeslotAdditionException If the Timeslot could not be added.
     * @throws ValidationException If the provided start date, end date, time, duration, day, periodicity, room or session are invalid.
     */
    @Override
    public Timeslot addTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, Room room, Session session) throws TimeslotAdditionException, ValidationException {

        validateTimeslotRelatedParams(startDate, endDate, time, duration, day, periodicity, room, session);

        Timeslot timeslot;

        try {
            timeslot = timeslotRepository.createNewTimeslot(startDate, endDate, time, duration, day, periodicity, room, session);
        } catch (RepositoryOperationException | ValidationException e) {
            throw new TimeslotAdditionException("[TimeslotService] Failed adding timeslot!", e);
        }

        return timeslot;
    }

    /**
     * Adds a new Timeslot with the given start date, end date, time, duration, day, periodicity, room name and discipline name.
     * @param startDate The start date of the new Timeslot.
     * @param endDate The end date of the new Timeslot.
     * @param time The time of the new Timeslot.
     * @param duration The duration of the new Timeslot.
     * @param day The day of the new Timeslot.
     * @param periodicity The periodicity of the new Timeslot.
     * @param roomName The room name of the new Timeslot.
     * @param disciplineName The discipline name of the new Timeslot.
     * @return The newly created Timeslot.
     * @throws TimeslotAdditionException If the Timeslot could not be added.
     * @throws SessionNotFoundException If the session could not be found.
     * @throws DisciplineNotFoundException If the discipline could not be found.
     * @throws RoomNotFoundException If the room could not be found.
     * @throws ValidationException If the provided start date, end date, time, duration, day, periodicity, room name or discipline name are invalid.
     */
    @Override
    public Timeslot addTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, String roomName, String disciplineName) throws TimeslotAdditionException, SessionNotFoundException, DisciplineNotFoundException, RoomNotFoundException, ValidationException {

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

    /**
     * Deletes the Timeslot with the given id.
     * @param timeslotId The id of the Timeslot to be deleted.
     * @return True if the Timeslot was deleted, false otherwise.
     * @throws TimeslotNotFoundException If the Timeslot could not be found.
     * @throws TimeslotDeletionFailed If the Timeslot could not be deleted.
     * @throws ValidationException If the provided Timeslot id is invalid.
     */
    @Override
    public boolean deleteTimeslot(int timeslotId) throws TimeslotNotFoundException, TimeslotDeletionFailed, ValidationException {

        ValidationHelpers.requirePositiveOrZero(timeslotId, IllegalArgumentException.class, "[TimeslotService] Timeslot id cannot be negative!", null);

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

    /**
     * Deletes the Timeslot with the given start date, time, duration and room name.
     * @param startDate The start date of the Timeslot to be deleted.
     * @param time The time of the Timeslot to be deleted.
     * @param duration The duration of the Timeslot to be deleted.
     * @param roomName The room name of the Timeslot to be deleted.
     * @return True if the Timeslot was deleted, false otherwise.
     * @throws TimeslotNotFoundException If the Timeslot could not be found.
     * @throws TimeslotDeletionFailed If the Timeslot could not be deleted.
     * @throws RoomNotFoundException If the room could not be found.
     */
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

    /**
     * Deletes all Timeslots.
     * @return True if the Timeslots were deleted, false otherwise.
     * @throws TimeslotDeletionFailed If the Timeslots could not be deleted.
     */
    @Override
    public boolean deleteAll() throws TimeslotDeletionFailed {
        try {
            timeslotRepository.deleteMany(timeslotRepository.readAll());
        } catch (Exception e) {
            throw new TimeslotDeletionFailed(" [TimeslotService] Failed to delete timeslots.", e);
        }

        return true;
    }

    /**
     * Gets the Timeslot with the given id.
     * @param timeslotId The id of the Timeslot to be retrieved.
     * @return The Timeslot with the given id.
     * @throws TimeslotNotFoundException If the Timeslot could not be found.
     * @throws ValidationException If the provided Timeslot id is invalid.
     */
    @Override
    public Timeslot getTimeslotById(int timeslotId) throws TimeslotNotFoundException, ValidationException {

        ValidationHelpers.requirePositiveOrZero(timeslotId, IllegalArgumentException.class, "[TimeslotService] Timeslot id cannot be negative!", null);

        var timeslot = timeslotRepository.getById(timeslotId);
        if (timeslot == null) {
            throw new TimeslotNotFoundException(MessageFormat.format("[TimeslotService] Timeslot with id {0} not found.", timeslotId));
        }
        return timeslot;
    }

    /**
     * Retrieves all Timeslots.
     * @return A list of all Timeslots.
     */
    @Override
    public List<Timeslot> getTimeslots() {
        return timeslotRepository.readAll();
    }

    /**
     * Retrieves all Timeslots sorted by start date and time.
     * @return A list of all Timeslots sorted by start date and time.
     */
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

    /**
     * Retrieves all Timeslots sorted by start time.
     * @return A list of all Timeslots sorted by start time.
     */
    @Override
    public List<Timeslot> getSortedTimeslotsByStartTime() {
        return getTimeslots()
                .stream()
                .sorted(
                        Comparator
                                .comparing(Timeslot::getTime))
                .toList();
    }

    /**
     * Validates Timeslot related parameters.
     * @param startDate The start date of the Timeslot.
     * @param endDate The end date of the Timeslot.
     * @param time The time of the Timeslot.
     * @param duration The duration of the Timeslot.
     * @param day The day of the Timeslot.
     * @param periodicity The periodicity of the Timeslot.
     * @param room The room of the Timeslot.
     * @param session The session of the Timeslot.
     * @throws ValidationException If any of the parameters are invalid.
     */
    private void validateTimeslotRelatedParams(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                               @Valid Room room, @Valid Session session) throws ValidationException {
        ValidationHelpers.requireNotNull(startDate, IllegalArgumentException.class, "Start date cannot be null.", null);
        ValidationHelpers.requireNotNull(endDate, IllegalArgumentException.class, "End date cannot be null.", null);
        ValidationHelpers.requireNotNull(time, IllegalArgumentException.class, "Time cannot be null.", null);
        ValidationHelpers.requireNotNull(duration, IllegalArgumentException.class, "Duration cannot be null.", null);
        ValidationHelpers.requireNotNull(day, IllegalArgumentException.class, "Day cannot be null.", null);
        ValidationHelpers.requireNotNull(periodicity, IllegalArgumentException.class, "Periodicity cannot be null.", null);
        ValidationHelpers.requireNotNull(room, IllegalArgumentException.class, "Room cannot be null.", null);
        ValidationHelpers.requireNotNull(session, IllegalArgumentException.class, "Session cannot be null.", null);

        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be after end date.");
        }
        if (startDate.equals(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be equal to end date.");
        }
        if (duration.isNegative()) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Duration cannot be negative.");
        }
    }

    /**
     * Validates Timeslot related parameters.
     * @param startDate The start date of the Timeslot.
     * @param endDate The end date of the Timeslot.
     * @param time The time of the Timeslot.
     * @param duration The duration of the Timeslot.
     * @param day The day of the Timeslot.
     * @param periodicity The periodicity of the Timeslot.
     * @param room The room of the Timeslot.
     * @param disciplineName The discipline name of the Timeslot.
     * @throws ValidationException If any of the parameters are invalid.
     */
    private void validateTimeslotRelatedParams(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                               String room, String disciplineName) throws ValidationException {
        ValidationHelpers.requireNotNull(startDate, IllegalArgumentException.class, "Start date cannot be null.", null);
        ValidationHelpers.requireNotNull(endDate, IllegalArgumentException.class, "End date cannot be null.", null);
        ValidationHelpers.requireNotNull(time, IllegalArgumentException.class, "Time cannot be null.", null);
        ValidationHelpers.requireNotNull(duration, IllegalArgumentException.class, "Duration cannot be null.", null);
        ValidationHelpers.requireNotNull(day, IllegalArgumentException.class, "Day cannot be null.", null);
        ValidationHelpers.requireNotNull(periodicity, IllegalArgumentException.class, "Periodicity cannot be null.", null);
        ValidationHelpers.requireNotBlank(room, IllegalArgumentException.class, "Room cannot be blank.", null);
        ValidationHelpers.requireNotBlank(disciplineName, IllegalArgumentException.class, "Discipline name cannot be blank.", null);


        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be after end date.");
        }

        if (startDate.equals(endDate)) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Start date cannot be equal to end date.");
        }

        if (duration.isNegative()) {
            throw new IllegalArgumentException("[TimeslotsService Validation] Duration cannot be negative.");
        }
    }
}
