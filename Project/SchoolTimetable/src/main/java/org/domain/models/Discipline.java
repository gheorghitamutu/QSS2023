package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Discipline")
@Table(name = "discipline", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@NamedQuery(name = "Discipline.getByName", query = "SELECT d FROM Discipline d WHERE d.name = :name")
public class Discipline implements Serializable {

    @ManyToMany(mappedBy = "disciplines", cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(mappedBy = "disciplines", cascade=CascadeType.ALL)
    private Set<Teacher> teachers = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false, unique=true)
    private String name;

    @Column(name = "Credits", nullable = false)
    private int credits;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Discipline() {
    }

    public Discipline(String name, int credits){
        this.name = name;
        this.credits = credits;
    }

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

    public void setStudents(Set<@Valid Student> students) {
        this.students = students;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<@Valid Session> sessions) {
        this.sessions = sessions;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }
}
