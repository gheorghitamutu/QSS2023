package org.application.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Student")
@Table(name = "student", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Student implements Serializable {

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Student_Discipline",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    Set<Discipline> disciplines = new HashSet<>();

    // @ManyToMany(cascade = {CascadeType.ALL})
    // @JoinTable(
    //         name = "Student_Session",
    //         joinColumns = {@JoinColumn(name = "student_id")},
    //         inverseJoinColumns = {@JoinColumn(name = "session_id")})
    // private Set<Session> sessions = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "\"Group\"", nullable = false)
    private String group;

    @Column(name = "\"Year\"", nullable = false)
    private int year;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    // public Set<Session> getSessions() {
    //     return sessions;
    // }
//
    // public void setSessions(Set<Session> sessions) {
    //     this.sessions = sessions;
    // }
}
