package org.domain.models.validators.student;

import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.GuiceInjectorSingleton;
import org.dataaccess.discipline.IDisciplineRepository;
import org.dataaccess.studentgroup.IStudentGroupRepository;
import org.domain.models.Student;
import org.domain.models.StudentGroup;
import org.domain.models.Discipline;

import java.util.Set;

/**
 * This class is the validator for the ValidStudent annotation.
 */
public class StudentValidator implements ConstraintValidator<ValidStudent, Student> {

    /**
     * The discipline repository.
     */
    private IDisciplineRepository disciplineRepository;
    /**
     * The student group repository.
     */
    private IStudentGroupRepository studentGroupRepository;

    /**
     * This method initializes the discipline repository and the student group repository.
     * @param constraintAnnotation The annotation.
     */
    @Override
    public void initialize(ValidStudent constraintAnnotation) {
        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();

        if(null != injector) {
            disciplineRepository = injector.getInstance(IDisciplineRepository.class);
            studentGroupRepository = injector.getInstance(IStudentGroupRepository.class);
        }
    }

    /**
     * This method checks if the student is valid.
     * @param value The student.
     * @param context The context.
     * @return True if the student is valid, false otherwise.
     */
    @Override
    public boolean isValid(Student value, ConstraintValidatorContext context) {

        Set<Discipline> disciplines = value.getDisciplines();
        for (Discipline discipline : disciplines) {
            if (!disciplineRepository.validate(discipline)) {
                return false;
            }
        }

        StudentGroup studentGroup = value.getGroup();
        return studentGroupRepository.validate(studentGroup);
    }
}

