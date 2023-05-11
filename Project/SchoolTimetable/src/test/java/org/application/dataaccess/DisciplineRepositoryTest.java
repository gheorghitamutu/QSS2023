package org.application.dataaccess;

import org.application.Application;
import org.application.GuiceInjectorSingleton;
import org.application.Main;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.session.SessionRepository;
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
import org.application.domain.models.Session;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DisciplineRepositoryTest {

    private DisciplineRepository disciplineRepository;
    private SessionRepository sessionRepository;
    private Application app;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();
        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
        new TimetableEntitiesFactory(app).createTimetableEntities();

        sessionRepository = new SessionRepository(new TestsDatabaseHibernateProvider());
        disciplineRepository = new DisciplineRepository(new TestsDatabaseHibernateProvider());
    }


    @AfterAll
    void tearDownAll() throws TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed {
        app.disciplinesService.deleteAll();
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.studentsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
    }



    //    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException {

    @Test
    public void Given__RoomRepository__When__createNewDisciplineIsCalled__Then__PersistDesciplineAndReturnValidDiscipline() throws RepositoryOperationException {
        Discipline discipline = disciplineRepository.createNewDiscipline("testDiscipline", 6);

        Assertions.assertEquals("testDiscipline", discipline.getName());
        Assertions.assertEquals(6, discipline.getCredits());
        Assertions.assertNotEquals(0, discipline.getId());
    }

    @Test
    public void getByName() throws RepositoryOperationException {
        Discipline discipline = disciplineRepository.getByName("Discipline 02");
        Assertions.assertEquals("Discipline 02", discipline.getName());
    }

    @Test
    public void saveDiscipline() throws RepositoryOperationException {

        Session session = new Session();
        session.setType(Session.Type.COURSE);
        session.setInsertTime(new Date());

        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setSessions(Collections.singleton(session));
        discipline.setInsertTime(new Date());

        session.setDiscipline(discipline);

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A7");
        studentGroup.setYear(1);
        studentGroup.setType(StudentGroup.Type.BACHELOR);
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setRegistrationNumber("310910204006SM000100");
        student.setInsertTime(new Date());

        studentGroup.setSessions(Collections.singleton(session));
        session.setGroups(Collections.singleton(studentGroup));

        disciplineRepository.save(discipline);
    }

    @Test()
    public void readDiscipline() {
        List<Discipline> disciplines = disciplineRepository.readAll();

        Assertions.assertTrue(disciplines.size() > 4);

        for (Discipline d : disciplines) {
            System.out.println(d);
        }
    }
}
