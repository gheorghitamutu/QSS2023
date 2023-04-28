package org.application.dataaccess;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.studentgroup.StudentGroupRepository;
import org.application.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentGroupRepositoryTest {
    private StudentGroupRepository studentGroupRepository;

    @BeforeAll
    public void setup() throws Exception {
    }

    @AfterAll
    void tearDownForAll() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        List<StudentGroup> groups = studentGroupRepository.readAll();
        studentGroupRepository.deleteMany(groups);
    }

    @BeforeAll
    void setUpAll() {
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        studentGroupRepository = new StudentGroupRepository(provider);
    }

    @AfterAll
    void tearDownAll() {
    }

    @Test
    public void saveGroup() {
        StudentGroup group = new StudentGroup();
        group.setName("A1");
        group.setInsertTime(new Date());
        Assertions.assertTrue(studentGroupRepository.save(group));
    }

    @Test()
    public void readGroup() {
        saveGroup();

        List<StudentGroup> groups = studentGroupRepository.readAll();
        Assertions.assertEquals(1, groups.size());
        for (StudentGroup sg : groups) {
            System.out.println(sg);
        }
    }
}
