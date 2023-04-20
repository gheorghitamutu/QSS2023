package org.example;

import org.application.DatabaseManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.application.Message;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageTest {
    DatabaseManager dbManager;

    @BeforeAll
    public void setup() throws Exception {
        try {
            dbManager = new DatabaseManager();
        }
        catch (Exception e) {
            // TODO: ?
        }
    }

    @AfterAll
    void tearDownForAll() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getId() {
    }

    @Test
    void setId() {
    }

    @Test
    void getText() {
    }

    @Test
    void setText() {
    }

    @Test
    void testToString() {
    }

    @Test
    public void saveMessage() {
        Message message = new Message("Hello, world");
        Assertions.assertTrue(dbManager.saveMessage(message));
    }

    @Test()
    public void readMessage() {
        List<Message> messages = dbManager.readMessages();
        Assertions.assertEquals(messages.size(), 1);
        for (Message m : messages) {
            System.out.println(m);
        }
    }
}