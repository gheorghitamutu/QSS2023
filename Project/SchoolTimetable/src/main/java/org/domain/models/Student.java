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

/**
 * This class represents a student.
 */
@ValidStudent
@Entity(name = "Student")
@Table(name = "student", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Student implements Serializable {

    /**
     * The disciplines that the student is attending.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid disciplines.
     */
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Student_Discipline", joinColumns = {@JoinColumn(name = "student_id")}, inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    Set<@Valid Discipline> disciplines = new HashSet<>();

    /**
     * The student group that the student is attending.
     * It is a many to one relationship.
     * It is a bidirectional relationship.
     * It is a valid student group.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "studentgroup_id", referencedColumnName = "Id")
    private StudentGroup studentGroup;

    /**
     * The id of the student.
     * It is not null.
     * It is unique.
     * It is an integer.
     * It is auto generated.
     * It is the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    /**
     * The name of the student.
     * It is not null.
     * It is a string.
     */
    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false, unique=true)
    private String name;

    /**
     * The year of the student.
     * It is not null.
     * It is an integer.
     * It must be greater than 0.
     * It must be less than 4.
     */
    @Min(value = 1, message = "Year must be greater than 0")
    @Max(value = 3, message = "Year must be less than 4")
    @Column(name = "\"Year\"", nullable = false)
    private int year;

    /**
     * The registration number of the student.
     * It is not null.
     * It is a 20 character string.
     * It is a 12 digit number followed by 2 capital letters followed by 6 digits.
     * It must not be blank.
     */
    @Length(min = 20, max = 20)
    @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
    @NotBlank(message = "Registration number must not be blank")
    @Column(name = "Registration_Number", nullable = false)
    private String registrationNumber;

    /**
     * The time when the student was inserted in the database.
     * It is not null.
     */
    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    /**
     * The default constructor of the student.
     */
    public Student() {
        this.insertTime = new Date();
    }

    /**
     * The constructor of the student.
     * @param name The name of the student.
     * @param year The year of the student.
     * @param registrationNumber The registration number of the student.
     */
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

    /**
     * Gets the id of the student.
     * @return The id of the student.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the student.
     * @param id The id of the student.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the student.
     * @return The name of the student.
     */
    @NotBlank(message = "Name must not be blank")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the student.
     * @param name The name of the student.
     * @throws IllegalArgumentException If the name is blank.
     */
    public void setName(
            @NotBlank(message = "Name must not be blank")
            String name) {
        this.name = name;
    }

    /**
     * Gets the year of the student.
     * @return The year of the student.
     */
    @Min(value = 1, message = "Year must be greater than 0")
    @Max(value = 3, message = "Year must be less than 4")
    public int getYear() {
        return year;
    }

    /**
     * Sets the year of the student.
     * @param year The year of the student.
     * @throws IllegalArgumentException If the year is less than 1 or greater than 3.
     */
    public void setYear(
            @Min(value = 1, message = "Year must be greater than 0")
            @Max(value = 3, message = "Year must be less than 4")
            int year) {
        this.year = year;
    }

    /**
     * Gets the insert time of the student.
     * @return The insert time of the student.
     */
    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * Sets the insert time of the student.
     * @param insertTime The insert time of the student.
     */
    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * Gets the disciplines of the student.
     * @return The disciplines of the student.
     */
    public Set<@Valid Discipline> getDisciplines() {
        return disciplines;
    }

    /**
     * Sets the disciplines of the student.
     * @param disciplines The disciplines of the student.
     * @throws IllegalArgumentException If the disciplines are empty.
     */
    public void setDisciplines(
            @NotEmpty(message = "Disciplines must not be empty")
            Set<@Valid Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    /**
     * Gets the group of the student.
     * @return The group of the student.
     */
    @Valid
    public StudentGroup getGroup() {
        return studentGroup;
    }

    /**
     * Sets the group of the student.
     * @param studentGroup The group of the student.
     */
    public void setGroup(@Valid StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    /**
     * Gets the registration number of the student.
     * @return The registration number of the student.
     */
    @Length(min = 20, max = 20)
    @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
    @NotBlank(message = "Registration number must not be blank")
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Sets the registration number of the student.
     * @param registrationNumber The registration number of the student.
     * @throws IllegalArgumentException If the registration number is blank.
     */
    public void setRegistrationNumber(
            @Length(min = 20, max = 20)
            @Pattern(regexp = "[0-9]{12}[A-Z]{2}[0-9]{6}")
            @NotBlank(message = "Registration number must not be blank")
            String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
