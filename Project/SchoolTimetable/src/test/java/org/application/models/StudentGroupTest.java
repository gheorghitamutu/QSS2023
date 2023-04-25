package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentGroupTest {
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
        List<StudentGroup> groups = DatabaseManager.readAll(StudentGroup.class);
        DatabaseManager.deleteMany(groups);
    }

    @BeforeAll
    void setUpAll() {
    }

    @AfterAll
    void tearDownAll() {
    }

    @Test
    public void saveGroup() {
        StudentGroup group = new StudentGroup();
        group.setName("A1");
        group.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(group));
    }

    @Test()
    public void readGroup() {
        saveGroup();

        List<StudentGroup> groups = DatabaseManager.readAll(StudentGroup.class);
        Assertions.assertEquals(1, groups.size());
        for (StudentGroup sg : groups) {
            System.out.println(sg);
        }
    }
}
