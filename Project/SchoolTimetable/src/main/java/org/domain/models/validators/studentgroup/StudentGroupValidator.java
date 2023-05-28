package org.domain.models.validators.studentgroup;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.domain.models.StudentGroup;
import org.domain.models.Timeslot;
import org.domain.models.Session;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is the validator for the ValidStudentGroup annotation.
 */
public class StudentGroupValidator implements ConstraintValidator<ValidStudentGroup, StudentGroup> {

    /**
     * This method initializes the annotation.
     * @param constraintAnnotation The annotation.
     */
    @Override
    public void initialize(ValidStudentGroup constraintAnnotation) {
    }

    /**
     * This method checks if the student group is valid.
     * @param value The student group.
     * @param context The context.
     * @return True if the student group is valid, false otherwise.
     */
    @Override
    public boolean isValid(StudentGroup value, ConstraintValidatorContext context) {

        Set<Timeslot> timeslots = new HashSet<>();
        for (Session session : value.getSessions()) {
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

