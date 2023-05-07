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
            var vsd = t1.getStartDate();
            var vt = t1.getTime();

            for (Timeslot t2 : timeslots) {
                if (t1.equals(t2)) {
                    continue;
                }

                var tsd = t2.getStartDate();
                var ted = t2.getEndDate();
                var tt = t2.getTime();
                var tet = Date.from(tt.toInstant().plus(t2.getTimespan()));

                if (t1.getWeekday() == t2.getWeekday()) {
                    if (vsd.after(tsd) && vsd.before(ted)) {
                        if (vt.after(tt) && vt.before(tet)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}

