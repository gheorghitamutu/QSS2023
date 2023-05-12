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

