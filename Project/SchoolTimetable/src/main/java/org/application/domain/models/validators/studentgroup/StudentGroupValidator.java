package org.application.domain.models.validators.studentgroup;

import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.MainDatabaseHibernateProvider;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.domain.models.StudentGroup;
import org.application.domain.models.Timeslot;
import org.application.domain.models.Session;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class StudentGroupValidator implements ConstraintValidator<ValidStudentGroup, StudentGroup> {

    private ISessionRepository sessionRepository;

    @Override
    public void initialize(ValidStudentGroup constraintAnnotation) {
        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();
        if(null != injector) {
            sessionRepository = injector.getInstance(ISessionRepository.class);
        }
    }

    @Override
    public boolean isValid(StudentGroup value, ConstraintValidatorContext context) {

        Set<Session> sessions = value.getSessions();
        for (Session session : sessions) {
            if (!sessionRepository.validate(session)) {
                return false;
            }
        }

        Set<Timeslot> timeslots = new HashSet<>();
        for (Session session : sessions) {
            timeslots.addAll(session.getTimeslots());
        }

        for (Timeslot t1 : timeslots) {
            Date d1 = t1.getTime();
            Duration t11 = t1.getTimespan();

            Calendar c1 = Calendar.getInstance();
            c1.setTime(d1);
            c1.add(Calendar.MINUTE, (int) t11.toMinutes());

            Date ed1 = c1.getTime();

            for (Timeslot t2 : timeslots) {

                Date d2 = t2.getTime();
                Duration t22 = t2.getTimespan();

                Calendar c2 = Calendar.getInstance();
                c2.setTime(d2);
                c2.add(Calendar.MINUTE, (int) t22.toMinutes());

                Date ed2 = c2.getTime();

                if (d1.after(d2) && d1.before(ed1)) {
                    return false;
                }

                if (ed1.after(d1) && ed1.before(ed2)) {
                    return false;
                }
            }
        }

        return true;
    }
}

