package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.domain.models.validators.student.ValidStudent;
import org.hibernate.validator.constraints.Length;

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

    @Column(name = "Name", nullable = false, unique=true)
    private String name;

    @Column(name = "\"Year\"", nullable = false)
    private int year;

    @Length(min = 20, max = 20)
    @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
    @Column(name = "Registration_Number", nullable = false)
    private String registrationNumber;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Student() {
    }

    public Student(String name, int year, String registrationNumber) {
        this.name = name;
        this.year = year;
        this.registrationNumber = registrationNumber;
        this.insertTime = new Date();
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
