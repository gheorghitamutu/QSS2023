package org.application;



import org.application.models.Teacher;

import jakarta.persistence.*;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
//        System.out.print("Hello and welcome!");
//
//        for (int i = 1; i <= 5; i++) {
//            System.out.println("i = " + i);
//        }

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();



        entityManager.getTransaction().begin();

        var t = new Teacher();

        t.setType("type");
        t.setInsertTime(new Date());
        t.setName("nic");

        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }
}
