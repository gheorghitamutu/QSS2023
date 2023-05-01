package org.application.application.timeslots;

import com.google.inject.Inject;
import org.application.dataaccess.timeslot.ITimeslotRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.application.domain.models.Timeslot;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class TimeslotsService implements ITimeslotsService {

    private final ITimeslotRepository timeslotRepository;

    @Inject
    public TimeslotsService(ITimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }

    @Override
    public Timeslot addTimeslot(Date date, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity) throws TimeslotAdditionException {
        Timeslot timeslot;

        try {
            timeslot = timeslotRepository.createNewTimeslot(date, duration, day, periodicity);
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
    public boolean deleteTimeslot(int timeslotId) throws TimeslotNotFoundException, TimeslotDeletionFailed {
        var timeslot = timeslotRepository.getById(timeslotId);
        if (timeslot == null) {
            throw new TimeslotNotFoundException(MessageFormat.format("[TimeslotService] Discipline with id {0} not found.", timeslotId));
        }

        try {
            timeslotRepository.delete(timeslot);
        } catch (Exception e) {
            throw new TimeslotDeletionFailed(" [TimeslotService] Failed to delete timeslot.", e);
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
}
