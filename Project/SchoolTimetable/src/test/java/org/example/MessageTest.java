package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageTest {
    SessionFactory sessionFactory;

    @BeforeAll
    public void setup() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).getMetadataBuilder().build().getSessionFactoryBuilder().build();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
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
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(message);
        tx.commit();
        session.close();
    }

    @Test()
    public void readMessage() {
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Message> list = (List<Message>) session.createQuery("from Message").list();

        Assertions.assertEquals(list.size(), 1);
        for (Message m : list) {
            System.out.println(m);
        }
        session.close();
    }
}