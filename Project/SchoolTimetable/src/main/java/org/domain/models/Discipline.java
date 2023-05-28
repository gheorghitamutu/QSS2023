package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a discipline.
 */
@Entity(name = "Discipline")
@Table(name = "discipline", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@NamedQuery(name = "Discipline.getByName", query = "SELECT d FROM Discipline d WHERE d.name = :name")
public class Discipline implements Serializable {

    /**
     * The students that are enrolled in the discipline.
     * It is a set of students.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid students.
     */
    @ManyToMany(mappedBy = "disciplines", cascade = CascadeType.ALL)
    private Set<@Valid Student> students = new HashSet<>();

    /**
     * The sessions of the discipline.
     * It is a one to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid sessions.
     */
    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Session> sessions = new HashSet<>();

    /**
     * The teachers that teach the discipline.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid teachers.
     */
    @ManyToMany(mappedBy = "disciplines", cascade=CascadeType.ALL)
    private Set<@Valid Teacher> teachers = new HashSet<>();

    /**
     * The id of the discipline.
     * It is a primary key.
     * It is auto generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    /**
     * The name of the discipline.
     * It is not null.
     * It is unique.
     * It is a string.
     */
    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false, unique=true)
    private String name;

    /**
     * The credits of the discipline.
     * It is not null.
     * It is a positive integer.
     */
    @Positive(message = "Credits must be positive")
    @Column(name = "Credits", nullable = false)
    private int credits;

    /**
     * The insert time of the discipline.
     * It is not null.
     * It is a date.
     */
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    /**
     * The default constructor of the discipline.
     * It initializes the insert time with the current date.
     */
    public Discipline() {
        this.insertTime = new Date();
    }

    /**
     * The constructor of the discipline.
     * It initializes the name and the credits of the discipline.
     * @param name The name of the discipline.
     * @param credits The credits of the discipline.
     */
    public Discipline(
            @NotBlank(message = "Name must not be blank")
            String name,
            @Positive(message = "Credits must be positive")
            int credits){
        this.name = name;
        this.credits = credits;
    }

    /**
     * Gets the id of the discipline.
     * @return The id of the discipline.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the discipline.
     * @param id The id of the discipline.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the discipline.
     * @return The name of the discipline.
     */
    @NotBlank(message = "Name must not be blank")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the discipline.
     * @param name The name of the discipline.
     */
    public void setName(
            @NotBlank(message = "Name must not be blank")
            String name
    ) {
        this.name = name;
    }

    /**
     * Gets the credits of the discipline.
     * @return The credits of the discipline.
     */
    @Positive(message = "Credits must be positive")
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the credits of the discipline.
     * @param credits The credits of the discipline.
     */
    public void setCredits(
            @Positive(message = "Credits must be positive")
            int credits
    ) {
        this.credits = credits;
    }

    /**
     * Sets the insert time of the discipline.
     * @param insertTime The insert time of the discipline.
     */
    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime
    ) {
        this.insertTime = insertTime;
    }

    /**
     * Gets the insert time of the discipline.
     * @return The insert time of the discipline.
     */
    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * Gets the students that are enrolled in the discipline.
     * @return The students that are enrolled in the discipline.
     */
    public Set<@Valid Student> getStudents() {
        return students;
    }

    /**
     * Sets the students that are enrolled in the discipline.
     * @param students The students that are enrolled in the discipline.
     */
    public void setStudents(Set<
            @Valid
            @NotEmpty(message = "Students must not be empty")
                    Student> students
    ) {
        this.students = students;
    }

    /**
     * Gets the sessions of the discipline.
     * @return The sessions of the discipline.
     */
    public Set<@Valid Session> getSessions() {
        return sessions;
    }

    /**
     * Sets the sessions of the discipline.
     * @param sessions The sessions of the discipline.
     */
    public void setSessions(Set<
            @Valid
            @NotEmpty(message = "Sessions must not be empty")
                    Session> sessions) {
        this.sessions = sessions;
    }

    /**
     * Gets the teachers that teach the discipline.
     * @return The teachers that teach the discipline.
     */
    public Set<@Valid Teacher> getTeachers() {
        return teachers;
    }

    /**
     * Sets the teachers that teach the discipline.
     * @param teachers The teachers that teach the discipline.
     */
    public void setTeachers(Set<
            @Valid @NotEmpty(message = "Teachers must not be empty")
                    Teacher> teachers) {
        this.teachers = teachers;
    }
}
