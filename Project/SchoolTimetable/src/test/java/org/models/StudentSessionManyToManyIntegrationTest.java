package org.models;

import org.Application;
import org.GuiceInjectorSingleton;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.database.TestsDatabaseHibernateProvider;
import org.dataaccess.discipline.DisciplineRepository;
import org.dataaccess.session.SessionRepository;
import org.dataaccess.student.StudentRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Discipline;
import org.domain.models.Session;
import org.domain.models.Student;
import org.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentSessionManyToManyIntegrationTest {

    StudentRepository studentRepository;
    SessionRepository sessionRepository;
    DisciplineRepository disciplineRepository;
    private Application app;


    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();


        IHibernateProvider provider = new TestsDatabaseHibernateProvider();

        sessionRepository = new SessionRepository(provider);
        studentRepository = new StudentRepository(provider);
        disciplineRepository = new DisciplineRepository(provider);


        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
        new TimetableEntitiesFactory(app).createTimetableEntities();
    }

    @AfterAll
    void tearDownAll() throws RepositoryOperationException {
        List<Student> students = studentRepository.readAll();
        studentRepository.deleteMany(students);

        List<Discipline> disciplines = disciplineRepository.readAll();
        disciplineRepository.deleteMany(disciplines);

        List<Session> sessions = sessionRepository.readAll();
        sessionRepository.deleteMany(sessions);
    }

    @Test
    public void saveStudentDiscipline() throws RepositoryOperationException {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test_discipline2000");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setInsertTime(new Date());
        session.setType(Session.Type.SEMINARY);

        discipline.setSessions(Collections.singleton(session));
        disciplineRepository.save(discipline);

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("B1");
        studentGroup.setYear(1);
        studentGroup.setType(StudentGroup.Type.BACHELOR);
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setRegistrationNumber("310910204006SM023000");
        student.setInsertTime(new Date());
        student.setDisciplines(Collections.singleton(discipline));

        session.setDiscipline(discipline);
        sessionRepository.save(session);

        studentRepository.save(student);
    }
}
