package org.application.models;

import org.application.Application;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.student.StudentRepository;
import org.application.databaseseed.TimetableEntitiesFactory;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.models.Discipline;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
        new TimetableEntitiesFactory(app).createTimetableEntities();
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
    }
}
