package org.application.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.application.models.validators.studentgroup.ValidStudentGroup;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ValidStudentGroup
@Entity(name = "StudentGroup")
@Table(name = "studentgroup", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class StudentGroup implements Serializable {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "StudentGroup_Session", joinColumns = {@JoinColumn(name = "studentgroup_id", referencedColumnName = "Id")}, inverseJoinColumns = {@JoinColumn(name = "session_id", referencedColumnName = "Id")})
    private Set<Session> sessions = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Length(min = 2, max = 2)
    @Pattern(regexp = "[A-Z]{1}[0-9]{1}")
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<@Valid Student> students) {
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

    public void setSessions(Set<@Valid Session> sessions) {
        this.sessions = sessions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}