package org.application.dataaccess.timeslot;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.domain.models.Timeslot;

public class TimeslotRepository extends BaseRepository<Timeslot> implements ITimeslotRepository {

    @Inject
    public TimeslotRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
