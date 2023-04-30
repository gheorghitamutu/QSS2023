package org.application.models;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.student.StudentRepository;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
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
        disciplineRepository = new DisciplineRepository(provider);
        studentRepository = new StudentRepository(provider);
    }

    @AfterAll
    void tearDownAll() throws RepositoryOperationException {
        List<Student> students = studentRepository.readAll();
        studentRepository.deleteMany(students);

        List<Discipline> disciplines = disciplineRepository.readAll();
        disciplineRepository.deleteMany(disciplines);
    }

    @Test
    public void saveStudentDiscipline() throws RepositoryOperationException {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());
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
        student.setInsertTime(new Date());
        Set<Discipline> disciplines = new HashSet<>();
        disciplines.add(discipline);
        student.setDisciplines(disciplines);

        studentRepository.save(student);
    }
}
