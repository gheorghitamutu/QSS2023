package org.application.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Discipline")
@Table(name = "discipline", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Discipline implements Serializable {

    @ManyToMany(mappedBy = "disciplines")
    private Set<Student> students = new HashSet<>();

    // @OneToMany(mappedBy="discipline")
    // private Set<Session> sessions;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Credits", nullable = false)
    private int credits;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    // public Set<Session> getSessions() {
    //     return sessions;
    // }
//
    // public void setSessions(Set<Session> sessions) {
    //     this.sessions = sessions;
    // }
}
