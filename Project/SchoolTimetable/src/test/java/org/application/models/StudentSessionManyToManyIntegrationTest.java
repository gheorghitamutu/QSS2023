package org.application.models;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.dataaccess.student.StudentRepository;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;
import org.application.domain.models.Session;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentSessionManyToManyIntegrationTest {

    StudentRepository studentRepository;
    SessionRepository sessionRepository;
    DisciplineRepository disciplineRepository;

    @BeforeEach
    void setUp() {
        TestsDI.initializeDi();

    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        sessionRepository = new SessionRepository(provider);
        studentRepository = new StudentRepository(provider);
        disciplineRepository = new DisciplineRepository(provider);
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
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setInsertTime(new Date());
        session.setType(Session.Type.SEMINARY);

        discipline.setSessions(Collections.singleton(session));
        disciplineRepository.save(discipline);

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A1");
        studentGroup.setYear(1);
        studentGroup.setType(StudentGroup.Type.BACHELOR);
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setRegistrationNumber("310910204006SM000000");
        student.setInsertTime(new Date());
        student.setDisciplines(Collections.singleton(discipline));

        session.setDiscipline(discipline);
        sessionRepository.save(session);

        studentRepository.save(student);
    }
}
