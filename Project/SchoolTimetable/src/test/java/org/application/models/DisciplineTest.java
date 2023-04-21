package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DisciplineTest {

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
        List<Discipline> disciplines = DatabaseManager.read(Discipline.class);
        DatabaseManager.deleteMany(disciplines);
    }

    @Test
    public void saveDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(discipline));
    }

    @Test()
    public void readDiscipline() {
        List<Discipline> disciplines = DatabaseManager.read(Discipline.class);
        Assertions.assertEquals(1, disciplines.size());
        for (Discipline r : disciplines) {
            System.out.println(r);
        }
    }
}