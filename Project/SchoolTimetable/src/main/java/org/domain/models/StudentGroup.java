package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.domain.models.validators.studentgroup.ValidStudentGroup;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a student group.
 */
@ValidStudentGroup
@Entity(name = "StudentGroup")
@Table(
        name = "studentgroup",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"Id"}),
                @UniqueConstraint(columnNames = {"Name", "\"Year\""}),
        }
)
@NamedQuery(name = "StudentGroup.getByGroupName", query = "SELECT sg FROM StudentGroup sg WHERE sg.name = :name")
public class StudentGroup implements Serializable {

    /**
     * The students that are in the student group.
     * It is a one to many relationship.
     * It is a set of valid students.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Student> students = new HashSet<>();

    /**
     * The sessions that the student group is attending.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid sessions.
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "StudentGroup_Session", joinColumns = {@JoinColumn(name = "studentgroup_id", referencedColumnName = "Id")}, inverseJoinColumns = {@JoinColumn(name = "session_id", referencedColumnName = "Id")})
    private Set<@Valid Session> sessions = new HashSet<>();

    /**
     * The id of the student group.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    /**
     * The name of the student group.
     * It is not null.
     * It is a string.
     * It is a two character string.
     * It is a capital letter followed by a digit.
     * It must not be blank.
     */
    @Length(min = 2, max = 2)
    @Pattern(regexp = "[A-Z]{1}[0-9]{1}")
    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false)
    private String name;

    /**
     * The year of the student group.
     * It is not null.
     * It is an integer.
     * It is greater than 0.
     * It is less than 4.
     */
    @Min(value = 1, message = "Year must be greater than 0")
    @Max(value = 3, message = "Year must be less than 4")
    @Column(name = "\"Year\"", nullable = false)
    private int year;

    /**
     * The type of the student group.
     * It is not null.
     * It is an enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private StudentGroup.Type type;

    /**
     * The insert time of the student group.
     * It is not null.
     * It is a date.
     */
    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    /**
     * The StudentGroup constructor.
     */
    public StudentGroup() {
        this.insertTime = new Date();
    }

    /**
     * The StudentGroup constructor.
     *
     * @param name The name of the student group.
     * @param year The year of the student group.
     * @param type The type of the student group.
     */
    public StudentGroup(
            @NotBlank(message = "Name must not be blank")
            String name,
            @Min(value = 1, message = "Year must be greater than 0")
            @Max(value = 3, message = "Year must be less than 4")
            int year,
            Type type) {
        this.name = name;
        this.year = year;
        this.type = type;
        this.insertTime = new Date();
    }

    /**
     * Gets the students of the student group.
     * @return The students of the student group.
     */
    public Set<@Valid Student> getStudents() {
        return students;
    }

    /**
     * Sets the students of the student group.
     * @param students The students of the student group.
     */
    public void setStudents(
            @NotEmpty(message = "Students must not be empty")
            Set<@Valid Student> students) {
        this.students = students;
    }

    /**
     * Gets the name of the student group.
     * @return The name of the student group.
     */
    @NotEmpty(message = "Name must not be empty")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the student group.
     * @param name The name of the student group.
     */
    public void setName(
            @NotEmpty(message = "Name must not be empty")
            String name) {
        this.name = name;
    }

    /**
     * Gets the insert time of the student group.
     * @return The insert time of the student group.
     */
    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * Sets the insert time of the student group.
     * @param insertTime The insert time of the student group.
     */
    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * Get the sessions of the student group.
     * @return The sessions of the student group.
     */
    public Set<@Valid Session> getSessions() {
        return sessions;
    }

    /**
     * Sets the sessions of the student group.
     * @param sessions The sessions of the student group.
     */
    public void setSessions(
            @NotEmpty(message = "Sessions must not be empty")
            Set<@Valid Session> sessions) {
        this.sessions = sessions;
    }

    /**
     * Gets the id of the student group.
     * @return The id of the student group.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the student group.
     * @param id The id of the student group.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the year of the student group.
     * @return The year of the student group.
     */
    @Min(value = 1, message = "Year must be greater than 0")
    @Max(value = 3, message = "Year must be less than 4")
    public int getYear() {
        return year;
    }

    /**
     * Sets the year of the student group.
     * @param year The year of the student group.
     */
    public void setYear(
            @Min(value = 1, message = "Year must be greater than 0")
            @Max(value = 3, message = "Year must be less than 4")
            int year) {
        this.year = year;
    }

    /**
     * Gets the type of the student group.
     * @return The type of the student group.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the student group.
     * @param type The type of the student group.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * The type of the student group.
     */
    public enum Type {
        BACHELOR, MASTER
    }
}