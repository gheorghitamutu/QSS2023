package org.application.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.application.models.validators.student.ValidStudent;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ValidStudent
@Entity(name = "Student")
@Table(name = "student", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Student implements Serializable {

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Student_Discipline", joinColumns = {@JoinColumn(name = "student_id")}, inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    Set<Discipline> disciplines = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "studentgroup_id", referencedColumnName = "Id")
    private StudentGroup studentGroup;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

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

    public void setDisciplines(Set<@Valid Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public StudentGroup getGroup() {
        return studentGroup;
    }

    public void setGroup(@Valid StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }
}
