package org.application.models;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Session")
@Table(name = "session", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Session implements Serializable {

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "Id")
    private Discipline discipline;

    @ManyToMany(mappedBy = "sessions")
    private Set<Student> students;

    @OneToMany(mappedBy = "session")
    private Set<Timeslot> timeslots = new HashSet<>();

    @ManyToMany(mappedBy = "sessions")
    private Set<Teacher> teachers;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Type", nullable = false)
    private int type;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
