package org.application.models;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.student.StudentRepository;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentDisciplineManyToManyIntegrationTest {

    StudentRepository studentRepository;
    DisciplineRepository disciplineRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        disciplineRepository = new DisciplineRepository(provider);
        studentRepository = new StudentRepository(provider);
    }

    @AfterAll
    void tearDownAll() {
        List<Student> students = studentRepository.readAll();
        studentRepository.deleteMany(students);

        List<Discipline> disciplines = disciplineRepository.readAll();
        disciplineRepository.deleteMany(disciplines);
    }

    @Test
    public void saveStudentDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());
        Assertions.assertTrue(disciplineRepository.save(discipline));

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A1");
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        Set<Discipline> disciplines = new HashSet<>();
        disciplines.add(discipline);
        student.setDisciplines(disciplines);

        Assertions.assertTrue(studentRepository.save(student));
    }
}
