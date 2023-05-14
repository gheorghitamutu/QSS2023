package org.models;

import org.Application;
import org.GuiceInjectorSingleton;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.database.TestsDatabaseHibernateProvider;
import org.dataaccess.discipline.DisciplineRepository;
import org.dataaccess.student.StudentRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.Discipline;
import org.domain.models.Student;
import org.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentDisciplineManyToManyIntegrationTest {

    StudentRepository studentRepository;
    DisciplineRepository disciplineRepository;
    private Application app;


    @BeforeAll
    void setUpAll() {

        TestsDI.initializeDi();

        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        disciplineRepository = new DisciplineRepository(provider);
        studentRepository = new StudentRepository(provider);


        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
    }

    @AfterAll
    void tearDownAll() throws RepositoryOperationException, TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed {
        app.disciplinesService.deleteAll();
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.studentsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
    }

    @Test
    public void saveStudentDiscipline() throws RepositoryOperationException {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test_discipline1999");
        discipline.setInsertTime(new Date());
        disciplineRepository.save(discipline);

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("B9");
        studentGroup.setYear(1);
        studentGroup.setType(StudentGroup.Type.BACHELOR);
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test1999");
        student.setRegistrationNumber("310910204006SM100000");
        student.setInsertTime(new Date());
        Set<Discipline> disciplines = new HashSet<>();
        disciplines.add(discipline);
        student.setDisciplines(disciplines);

        studentRepository.save(student);

        Assertions.assertEquals(1, studentRepository.readAll().size());
    }
}
