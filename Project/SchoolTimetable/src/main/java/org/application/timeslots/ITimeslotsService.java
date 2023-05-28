package org.application.timeslots;

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

import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * Represents a service interface for managing Timeslot entities.
 * This interface defines the service operations that can be performed, specific to timeslot service.
 */
public interface ITimeslotsService {

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
    public Timeslot addTimeslot(Date startDate, Date endDate, Date time,
                                Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                Room room, Session session) throws TimeslotAdditionException, ValidationException;

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
    public Timeslot addTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day,
                                Timeslot.Periodicity periodicity, String roomName, String disciplineName) throws TimeslotAdditionException, SessionNotFoundException, DisciplineNotFoundException, RoomNotFoundException, ValidationException;

    /**
     * Deletes the Timeslot with the given id.
     * @param timeslotId The id of the Timeslot to be deleted.
     * @return True if the Timeslot was deleted, false otherwise.
     * @throws TimeslotNotFoundException If the Timeslot could not be found.
     * @throws TimeslotDeletionFailed If the Timeslot could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    public boolean deleteTimeslot(int timeslotId) throws TimeslotNotFoundException, TimeslotDeletionFailed, ValidationException;

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
    public boolean deleteTimeslot(Date startDate, Date time, Duration duration, String roomName) throws TimeslotNotFoundException, TimeslotDeletionFailed, RoomNotFoundException;

    /**
     * Deletes all Timeslots.
     * @return True if all Timeslots were deleted, false otherwise.
     * @throws TimeslotDeletionFailed If the Timeslots could not be deleted.
     */
    public boolean deleteAll() throws TimeslotDeletionFailed;

    /**
     * Gets the Timeslot with the given id.
     * @param timeslotId The id of the Timeslot to be retrieved.
     * @return The Timeslot with the given id.
     * @throws TimeslotNotFoundException If the Timeslot could not be found.
     * @throws ValidationException If the provided id is invalid.
     */
    public Timeslot getTimeslotById(int timeslotId) throws TimeslotNotFoundException, ValidationException;

    /**
     * Retrieves all Timeslots from the database.
     * @return A list of all Timeslots.
     */
    public List<Timeslot> getTimeslots();

    /**
     * Retrieves all Timeslots sorted by start date and time.
     * @return A list of all Timeslots sorted by start date and time.
     */
    public List<Timeslot> getSortedTimeslotsByStartDateAndTime();

    /**
     * Retrieves all Timeslots sorted by start time.
     * @return A list of all Timeslots sorted by start time.
     */
    public List<Timeslot> getSortedTimeslotsByStartTime();
}
