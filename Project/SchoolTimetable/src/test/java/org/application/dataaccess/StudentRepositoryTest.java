package org.application.dataaccess;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.student.StudentRepository;
import org.application.dataaccess.studentgroup.StudentGroupRepository;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentRepositoryTest {

    private StudentRepository studentRepository;
    private StudentGroupRepository studentGroupRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        studentRepository = new StudentRepository(provider);
        studentGroupRepository = new StudentGroupRepository(provider);
    }

    @AfterAll
    void tearDownAll() {
        List<StudentGroup> studentsGroups = studentGroupRepository.readAll();
        studentGroupRepository.deleteMany(studentsGroups);

        List<Student> students = studentRepository.readAll();
        studentRepository.deleteMany(students);
    }

    @Test
    public void saveStudent() {
        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A1");
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        Assertions.assertTrue(studentRepository.save(student));
    }

    @Test()
    public void readStudent() {
        List<Student> students = studentRepository.readAll();
        Assertions.assertEquals(1, students.size());
        for (Student r : students) {
            System.out.println(r);
        }
    }
}
