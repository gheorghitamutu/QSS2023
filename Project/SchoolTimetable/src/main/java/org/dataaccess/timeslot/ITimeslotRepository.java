package org.dataaccess.timeslot;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;

/**
 * This is the interface for Timeslot.
 */
public interface ITimeslotRepository extends IRepository<Timeslot> {

    /**
     * This is the method to create a new timeslot.
     * @param startDate The start date.
     * @param endDate The end date.
     * @param time The time.
     * @param duration The duration.
     * @param day The day.
     * @param periodicity The periodicity.
     * @param room The room.
     * @param session The session.
     * @return The timeslot.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Timeslot createNewTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, Room room, Session session) throws RepositoryOperationException, ValidationException;
}
