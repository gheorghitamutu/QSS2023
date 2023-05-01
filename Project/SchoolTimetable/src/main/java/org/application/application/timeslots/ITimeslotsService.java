package org.application.application.timeslots;

import org.application.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.application.domain.exceptions.teacher.TeacherAdditionException;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.Teacher;
import org.application.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public interface ITimeslotsService {

    public Timeslot addTimeslot(Date date, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity) throws TimeslotAdditionException;

    public boolean deleteTimeslot(int timeslotId) throws TimeslotNotFoundException, TimeslotDeletionFailed;

    public Timeslot getTimeslotById(int timeslotId) throws TimeslotNotFoundException;

    public List<Timeslot> getTimeslots();
}
