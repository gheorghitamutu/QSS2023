package org.application.domain.models.validators.teacher;

import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.MainDatabaseHibernateProvider;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.domain.models.Discipline;
import org.application.domain.models.Session;
import org.application.domain.models.Timeslot;
import org.application.domain.models.Teacher;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TeacherValidator implements ConstraintValidator<ValidTeacher, Teacher> {

    ISessionRepository sessionRepository;
    IDisciplineRepository disciplineRepository;

    @Override
    public void initialize(ValidTeacher constraintAnnotation) {

        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();
        if(null != injector) {
            sessionRepository = injector.getInstance(ISessionRepository.class);
            disciplineRepository = injector.getInstance(IDisciplineRepository.class);
        }
    }

    @Override
    public boolean isValid(Teacher value, ConstraintValidatorContext context) {

        Set<Session> sessions = value.getSessions();
        for (Session session : sessions) {
            if (!sessionRepository.validate(session)) {
                return false;
            }
        }

        Teacher.Type teacherType = value.getType();
        for (Session session : sessions) {
            if (session.getType() == Session.Type.COURSE && teacherType != Teacher.Type.TEACHER) {
                return false;
            }
        }

        Set<Discipline> disciplines = value.getDisciplines();
        for (Discipline discipline : disciplines) {
            if (!disciplineRepository.validate(discipline)) {
                return false;
            }

            if (!discipline.getTeachers().contains(value)) {
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

