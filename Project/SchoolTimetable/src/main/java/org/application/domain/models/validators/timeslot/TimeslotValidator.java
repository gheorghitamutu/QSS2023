package org.application.domain.models.validators.timeslot;

import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.MainDatabaseHibernateProvider;
import org.application.dataaccess.room.IRoomRepository;
import org.application.dataaccess.room.RoomRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.domain.models.Room;
import org.application.domain.models.Session;
import org.application.domain.models.Timeslot;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class TimeslotValidator implements ConstraintValidator<ValidTimeslot, Timeslot> {


    private IRoomRepository roomRepository;
    private ISessionRepository sessionRepository;

    @Override
    public void initialize(ValidTimeslot constraintAnnotation) {

        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();
        if(null != injector) {
            roomRepository = injector.getInstance(IRoomRepository.class);
            sessionRepository = injector.getInstance(ISessionRepository.class);
        }
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

