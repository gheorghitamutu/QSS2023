package org.application.dataaccess.timeslot;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;

public class TimeslotRepository extends BaseRepository<Timeslot> implements ITimeslotRepository {

    @Inject
    public TimeslotRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    @Override
    public Timeslot createNewTimeslot(Date date, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity) throws RepositoryOperationException {
        var timeslot = new Timeslot();

        timeslot.setTime(date);
        timeslot.setTimespan(duration);
        timeslot.setWeekday(day);
        timeslot.setPeriodicity(periodicity);
        timeslot.setInsertTime(new Date());

        try {
            save(timeslot);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryOperationException("[TimeslotRepository] Couldn't create new timeslot.", e);
        }

        return timeslot;
    }

}
