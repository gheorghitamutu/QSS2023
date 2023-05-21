package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    Set<@Valid Discipline> disciplines = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "studentgroup_id", referencedColumnName = "Id")
    private StudentGroup studentGroup;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false, unique=true)
    private String name;

    @Min(value = 1, message = "Year must be greater than 0")
    @Max(value = 3, message = "Year must be less than 4")
    @Column(name = "\"Year\"", nullable = false)
    private int year;

    @Length(min = 20, max = 20)
    @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
    @NotBlank(message = "Registration number must not be blank")
    @Column(name = "Registration_Number", nullable = false)
    private String registrationNumber;

    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Student() {
    }

    public Student(
            @NotBlank(message = "Name must not be blank")
            String name,
            @Min(value = 1, message = "Year must be greater than 0")
            @Max(value = 3, message = "Year must be less than 4")
            int year,
            @Length(min = 20, max = 20)
            @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
            @NotBlank(message = "Registration number must not be blank")
            String registrationNumber) {
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

    @NotBlank(message = "Name must not be blank")
    public String getName() {
        return name;
    }

    public void setName(
            @NotBlank(message = "Name must not be blank")
            String name) {
        this.name = name;
    }

    @Min(value = 1, message = "Year must be greater than 0")
    @Max(value = 3, message = "Year must be less than 4")
    public int getYear() {
        return year;
    }

    public void setYear(
            @Min(value = 1, message = "Year must be greater than 0")
            @Max(value = 3, message = "Year must be less than 4")
            int year) {
        this.year = year;
    }

    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    public Set<@Valid Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(
            @NotEmpty(message = "Disciplines must not be empty")
            Set<@Valid Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    @Valid
    public StudentGroup getGroup() {
        return studentGroup;
    }

    public void setGroup(@Valid StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    @Length(min = 20, max = 20)
    @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
    @NotBlank(message = "Registration number must not be blank")
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(
            @Length(min = 20, max = 20)
            @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
            @NotBlank(message = "Registration number must not be blank")
            String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
