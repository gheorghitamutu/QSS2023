package org.application.domain.models.validators.student;

import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;
import org.application.domain.models.Discipline;

import java.util.Set;

public class StudentValidator implements ConstraintValidator<ValidStudent, Student> {

    private IDisciplineRepository disciplineRepository;
    private IStudentGroupRepository studentGroupRepository;


    @Override
    public void initialize(ValidStudent constraintAnnotation) {
        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();

        if(null != injector) {
            disciplineRepository = injector.getInstance(IDisciplineRepository.class);
            studentGroupRepository = injector.getInstance(IStudentGroupRepository.class);
        }
    }

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

