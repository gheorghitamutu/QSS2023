package org.dataaccess.timeslot;

import com.google.inject.Inject;
import jakarta.validation.Valid;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;

public class TimeslotRepository extends BaseRepository<Timeslot> implements ITimeslotRepository {

    @Inject
    public TimeslotRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    @Override
    public Timeslot createNewTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, Room room, Session session) throws RepositoryOperationException {

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

    private void validateTimeslotRelatedParams(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity,
                                               @Valid Room room, @Valid Session session) throws RepositoryOperationException {
        if (startDate == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Start date cannot be null.");
        }

        if (endDate == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] End date cannot be null.");
        }

        if (startDate.after(endDate)) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Start date cannot be after end date.");
        }

        if (startDate.equals(endDate)) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Start date cannot be equal to end date.");
        }

        if (time == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Time cannot be null.");
        }

        if (duration == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Duration cannot be null.");
        }

        if (duration.isNegative()) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Duration cannot be negative.");
        }

        if (day == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Day cannot be null.");
        }

        if (periodicity == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Periodicity cannot be null.");
        }

        if (room == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Room cannot be null.");
        }

        if (session == null) {
            throw new RepositoryOperationException("[TimeslotRepository Validation] Session cannot be null.");
        }
    }

}
