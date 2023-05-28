package org.domain.models.validators.timeslot;

import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.GuiceInjectorSingleton;
import org.dataaccess.session.ISessionRepository;
import org.dataaccess.timeslot.ITimeslotRepository;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;

import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is the validator for the ValidTimeslot annotation.
 */
public class TimeslotValidator implements ConstraintValidator<ValidTimeslot, Timeslot> {

    /**
     * The session repository.
     */
    private ISessionRepository sessionRepository;

    /**
     * The timeslot repository.
     */
    private ITimeslotRepository timeslotRepository;

    /**
     * This method initializes the session repository and the timeslot repository.
     * @param constraintAnnotation The annotation.
     */
    @Override
    public void initialize(ValidTimeslot constraintAnnotation) {

        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();
        if (injector != null) {
            sessionRepository = injector.getInstance(ISessionRepository.class);
            timeslotRepository = injector.getInstance(ITimeslotRepository.class);
        }
    }

    /**
     * This method checks if the timeslot is valid.
     * @param value The timeslot.
     * @param context The context.
     * @return True if the timeslot is valid, false otherwise.
     */
    @Override
    public boolean isValid(Timeslot value, ConstraintValidatorContext context) {

        Room room = value.getRoom();
        if (room == null) {
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

        if (value.getStartDate().after(value.getEndDate())) {
            return false;
        }

        Date startTime = value.getTime();
        Duration timespan = value.getTimespan();
        Date endTime = Date.from(startTime.toInstant().plus(timespan));

        try {
            var startTimeHours = startTime.getHours();

            var endTimeHours = endTime.getHours();

            if (startTimeHours < 8 || startTimeHours > 20) {
                return false;
            }

            if (endTimeHours < 8 || endTimeHours > 20) {
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

        Set<Timeslot> timeslots = timeslotRepository.readAll().stream().filter(t -> t.getRoom().equals(room)).collect(Collectors.toSet());
        for (Timeslot timeslot : timeslots) {
            if (value == timeslot) { // don't check against current timeslot
                continue;
            }

            var vsd = value.getStartDate();
            var tsd = timeslot.getStartDate();
            var ted = timeslot.getEndDate();

            var vt = value.getTime();
            var tt = timeslot.getTime();
            var tet = Date.from(tt.toInstant().plus(timeslot.getTimespan()));

            if (value.getWeekday() == timeslot.getWeekday()) {
                if (vsd.equals(tsd) && vt.equals(tt)) {
                    return false;
                }
                if (vsd.after(tsd) && vsd.before(ted)) {
                    if (vt.after(tt) && vt.before(tet)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}

