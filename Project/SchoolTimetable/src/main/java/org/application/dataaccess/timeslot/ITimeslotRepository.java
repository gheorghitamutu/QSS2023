package org.application.dataaccess.timeslot;

import org.application.dataaccess.repository.IRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Teacher;
import org.application.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;

public interface ITimeslotRepository extends IRepository<Timeslot> {

    public Timeslot createNewTimeslot(Date date, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity) throws RepositoryOperationException;
}
