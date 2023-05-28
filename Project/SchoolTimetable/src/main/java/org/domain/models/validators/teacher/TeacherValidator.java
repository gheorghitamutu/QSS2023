package org.domain.models.validators.teacher;

import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.GuiceInjectorSingleton;
import org.dataaccess.discipline.IDisciplineRepository;
import org.dataaccess.session.ISessionRepository;
import org.domain.models.Discipline;
import org.domain.models.Session;
import org.domain.models.Timeslot;
import org.domain.models.Teacher;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is the validator for the ValidTeacher annotation.
 */
public class TeacherValidator implements ConstraintValidator<ValidTeacher, Teacher> {

    /**
     * The session repository.
     */
    ISessionRepository sessionRepository;

    /**
     * The discipline repository.
     */
    IDisciplineRepository disciplineRepository;

    /**
     * This method initializes the session repository and the discipline repository.
     * @param constraintAnnotation The annotation.
     */
    @Override
    public void initialize(ValidTeacher constraintAnnotation) {

        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();
        if (null != injector) {
            sessionRepository = injector.getInstance(ISessionRepository.class);
            disciplineRepository = injector.getInstance(IDisciplineRepository.class);
        }
    }

    /**
     * This method checks if the teacher is valid.
     * @param value The teacher.
     * @param context The context.
     * @return True if the teacher is valid, false otherwise.
     */
    @Override
    public boolean isValid(Teacher value, ConstraintValidatorContext context) {

        Set<Session> sessions = value.getSessions();
        Teacher.Type teacherType = value.getType();
        for (Session session : sessions) {
            if (session.getType() == Session.Type.COURSE && teacherType != Teacher.Type.TEACHER) {
                return false;
            }
        }

        Set<Discipline> disciplines = value.getDisciplines();
        for (Discipline discipline : disciplines) {
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

