package org.application.models.validators.student;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.MainDatabaseHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.studentgroup.StudentGroupRepository;
import org.application.models.Discipline;
import org.application.models.Student;
import org.application.models.StudentGroup;

import java.util.Set;

public class StudentValidator implements ConstraintValidator<ValidStudent, Student> {

    DisciplineRepository disciplineRepository;
    StudentGroupRepository studentGroupRepository;

    @Override
    public void initialize(ValidStudent constraintAnnotation) {
        IHibernateProvider provider = new MainDatabaseHibernateProvider();
        disciplineRepository = new DisciplineRepository(provider);
        studentGroupRepository = new StudentGroupRepository(provider);
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

