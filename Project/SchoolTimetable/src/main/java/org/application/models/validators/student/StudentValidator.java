package org.application.models.validators.student;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.DatabaseManager;
import org.application.models.Discipline;
import org.application.models.Student;
import org.application.models.StudentGroup;

import java.util.Set;

public class StudentValidator implements ConstraintValidator<ValidStudent, Student> {

    @Override
    public void initialize(ValidStudent constraintAnnotation) {
    }

    @Override
    public boolean isValid(Student value, ConstraintValidatorContext context) {

        Set<Discipline> disciplines = value.getDisciplines();
        for (Discipline discipline : disciplines) {
            if (!DatabaseManager.constraintValidation(discipline)) {
                return false;
            }
        }

        StudentGroup studentGroup = value.getGroup();
        return DatabaseManager.constraintValidation(studentGroup);
    }
}

