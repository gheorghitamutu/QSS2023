package org.application.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Group")
@Table(name = "group", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Group implements Serializable {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "groups")
    private Set<Session> sessions = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }
}