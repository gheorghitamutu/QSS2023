package org.dataaccess.timeslot;

import com.google.inject.Inject;
import jakarta.validation.Valid;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;

/**
 * This is the class for TimeslotRepository.
 */
public class TimeslotRepository extends BaseRepository<Timeslot> implements ITimeslotRepository {

    /**
     * This is the constructor of TimeslotRepository.
     * @param hibernateProvider The hibernate provider.
     */
    @Inject
    public TimeslotRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

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
    @Override
    public Timeslot createNewTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, Room room, Session session) throws RepositoryOperationException, ValidationException {

        validateTimeslotRelatedParams(startDate, endDate, time, duration, day, periodicity, room, session);

        var timeslot = new Timeslot();

        timeslot.setStartDate(startDate);
        timeslot.setEndDate(endDate);
        timeslot.setTime(time);
        timeslot.setTimespan(duration);
        timeslot.setWeekday(day);
        timeslot.setPeriodicity(periodicity);
        timeslot.setRoom(room);
        timeslot.setSession(session);
        timeslot.setInsertTime(new Date());

        try {
            save(timeslot);
        } catch (Exception e) {
            e.printStackTrace();

            throw new RepositoryOperationException("[TimeslotRepository] Couldn't create new timeslot.", e);
        }

        return timeslot;
    }

    /**
     * This is the method to validate timeslot related parameters.
     * @param startDate The start date.
     * @param endDate The end date.
     * @param time The time.
     * @param duration The duration.
     * @param day The day.
     * @param periodicity The periodicity.
     * @param room The room.
     * @param session The session.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    private void validateTimeslotRelatedParams(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                               @Valid Room room, @Valid Session session) throws RepositoryOperationException, ValidationException {
        ValidationHelpers.requireNotNull(startDate, IllegalArgumentException.class, "Start date cannot be null.", null);
        ValidationHelpers.requireNotNull(endDate, IllegalArgumentException.class, "End date cannot be null.", null);
        ValidationHelpers.requireNotNull(time, IllegalArgumentException.class, "Time cannot be null.", null);
        ValidationHelpers.requireNotNull(duration, IllegalArgumentException.class, "Duration cannot be null.", null);
        ValidationHelpers.requireNotNull(day, IllegalArgumentException.class, "Day cannot be null.", null);
        ValidationHelpers.requireNotNull(periodicity, IllegalArgumentException.class, "Periodicity cannot be null.", null);
        ValidationHelpers.requireNotNull(room, IllegalArgumentException.class, "Room cannot be null.", null);
        ValidationHelpers.requireNotNull(session, IllegalArgumentException.class, "Session cannot be null.", null);

        if (startDate.after(endDate)) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Start date cannot be after end date.");
        }

        if (startDate.equals(endDate)) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Start date cannot be equal to end date.");
        }

        if (duration.isNegative()) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Duration cannot be negative.");
        }
    }

}
