package org.application.dataaccess;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.teacher.TeacherRepository;
import org.application.models.Teacher;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherRepositoryTest {
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        teacherRepository = new TeacherRepository(provider);
    }

    @AfterAll
    void tearDownAll() {
        List<Teacher> teachers = teacherRepository.readAll();
        teacherRepository.deleteMany(teachers);
    }

    @Test
    public void saveTeacher() {
        Teacher teacher = new Teacher();
        teacher.setType(Teacher.Type.COLLABORATOR);
        teacher.setName("test");
        teacher.setInsertTime(new Date());
        Assertions.assertTrue(teacherRepository.save(teacher));
    }

    @Test()
    public void readTeacher() {
        List<Teacher> teachers = teacherRepository.readAll();
        Assertions.assertEquals(1, teachers.size());
        for (Teacher t : teachers) {
            System.out.println(t);
        }
    }
}