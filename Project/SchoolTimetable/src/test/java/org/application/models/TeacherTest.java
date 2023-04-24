package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
    }

    @AfterAll
    void tearDownAll() {
        List<Teacher> teachers = DatabaseManager.readAll(Teacher.class);
        DatabaseManager.deleteMany(teachers);
    }

    @Test
    public void saveTeacher() {
        Teacher teacher = new Teacher();
        teacher.setType("Colab");
        teacher.setName("test");
        teacher.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(teacher));
    }

    @Test()
    public void readTeacher() {
        List<Teacher> teachers = DatabaseManager.readAll(Teacher.class);
        Assertions.assertEquals(1, teachers.size());
        for (Teacher t : teachers) {
            System.out.println(t);
        }
    }
}
