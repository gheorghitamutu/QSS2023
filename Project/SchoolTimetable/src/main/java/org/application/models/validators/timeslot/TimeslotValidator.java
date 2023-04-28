package org.application.models.validators.timeslot;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.MainDatabaseHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.room.RoomRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.models.Room;
import org.application.models.Session;
import org.application.models.Timeslot;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class TimeslotValidator implements ConstraintValidator<ValidTimeslot, Timeslot> {

    RoomRepository roomRepository;
    SessionRepository sessionRepository;

    @Override
    public void initialize(ValidTimeslot constraintAnnotation) {
        IHibernateProvider provider = new MainDatabaseHibernateProvider();
        roomRepository = new RoomRepository(provider);
        sessionRepository = new SessionRepository(provider);
    }

    @Override
    public boolean isValid(Timeslot value, ConstraintValidatorContext context) {

        Room room = value.getRoom();
        if (room == null) {
            return false;
        }
        if (!roomRepository.validate(room)) {
            return false;
        }

        Session session = value.getSession();
        if (session.getType() == Session.Type.COURSE && room.getType() != Room.Type.COURSE) {
            return false;
        }
        if (!sessionRepository.validate(session)) {
            return false;
        }

        if (value.getTimespan().isZero()) {
            return false;
        }

        Date startDate = value.getTime();
        Duration timespan = value.getTimespan();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MINUTE, (int) timespan.toMinutes());

        Date endDate = calendar.getTime();

        try {
            if (startDate.before(new SimpleDateFormat("HH:mm:ss").parse("08:00:00"))) {
                return false;
            }

            if (endDate.after(new SimpleDateFormat("HH:mm:ss").parse("20:00:00"))) {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        Timeslot.Day day = value.getWeekday();
        if (day == Timeslot.Day.SATURDAY || day == Timeslot.Day.SUNDAY) {
            return false;
        }

        Set<Timeslot> timeslots = room.getTimeslots();
        for (Timeslot timeslot : timeslots) {
            if (value == timeslot) { // don't check against current timeslot
                continue;
            }

            Date d = timeslot.getTime();
            Duration t = timeslot.getTimespan();

            calendar.setTime(d);
            calendar.add(Calendar.MINUTE, (int) t.toMinutes());

            Date ed = calendar.getTime();

            if (startDate.after(d) && startDate.before(ed)) {
                return false;
            }

            if (endDate.after(d) && endDate.before(ed)) {
                return false;
            }
        }

        return true;
    }
}

