package org.application.models;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Session")
@Table(name = "session", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Session implements Serializable {

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "discipline_id", referencedColumnName = "Id")
    private Discipline discipline;

    @ManyToMany(mappedBy = "sessions", cascade=CascadeType.ALL)
    private Set<StudentGroup> studentGroups = new HashSet<>();

    @ManyToMany(mappedBy = "sessions", cascade=CascadeType.ALL)
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "session", cascade=CascadeType.ALL)
    private Set<Timeslot> timeslots = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Set<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<StudentGroup> getGroups() {
        return studentGroups;
    }

    public void setGroups(Set<StudentGroup> studentGroups) {
        this.studentGroups = studentGroups;
    }

    public enum Type {
        COURSE, LABORATORY, SEMINARY
    }
}
