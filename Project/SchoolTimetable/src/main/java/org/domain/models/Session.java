package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.domain.models.validators.session.ValidSession;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a session.
 */
@ValidSession
@Entity(name = "Session")
@Table(name = "session", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Session implements Serializable {

    /**
     * The discipline that the session is about.
     * It is a many to one relationship.
     * It is a bidirectional relationship.
     * It is a valid discipline.
     */
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "discipline_id", referencedColumnName = "Id")
    private Discipline discipline;

    /**
     * The student groups that the session is about.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid student groups.
     */
    @ManyToMany(mappedBy = "sessions", cascade=CascadeType.ALL)
    private Set<@Valid StudentGroup> studentGroups = new HashSet<>();

    /**
     * The teachers that the session is about.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid teachers.
     */
    @ManyToMany(mappedBy = "sessions", cascade=CascadeType.ALL)
    private Set<@Valid Teacher> teachers = new HashSet<>();

    /**
     * The timeslots that the session is about.
     * It is a one to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid timeslots.
     */
    @OneToMany(mappedBy = "session", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Timeslot> timeslots = new HashSet<>();

    /**
     * The id of the session.
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
     * The type of the session.
     * It is not null.
     * It is a valid type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    /**
     * The half year of the session.
     * It is not null.
     * It is a string.
     * It is a capital letter.
     * It is one character long.
     */
    @Length(min = 1, max = 1)
    @Pattern(regexp = "[A-Z]{1}")
    @NotBlank(message = "Half year must not be blank")
    @Column(name = "HalfYear")
    private String halfYear;

    /**
     * The insert time of the session.
     * It is not null.
     * It is a date.
     */
    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    /**
     * The default constructor of the session.
     * It initializes the insert time with the current date.
     */
    public Session() {
        this.insertTime = new Date();
    }

    /**
     * The constructor of the session.
     * It initializes the type, half year and insert time.
     * @param type The type of the session.
     * @param halfYear The half year of the session.
     */
    public Session(Type type,
                   @NotBlank(message = "Half year must not be blank")
                   String halfYear){
        this.type = type;
        this.halfYear = halfYear;
        this.insertTime = new Date();
    }

    /**
     * Gets the id of the session.
     * @return The id of the session.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the session.
     * @param id The id of the session.
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Gets the type of the session.
     * @return The type of the session.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the session.
     * @param type The type of the session.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets the insert time of the session.
     * @return The insert time of the session.
     */
    @NotNull(message = "Half year must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * Sets the insert time of the session.
     * @param insertTime The insert time of the session.
     * @throws IllegalArgumentException If the insert time is null.
     */
    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * Gets the discipline of the session.
     * @return The discipline of the session.
     */
    @Valid
    public Discipline getDiscipline() {
        return discipline;
    }

    /**
     * Sets the discipline of the session.
     * @param discipline The discipline of the session.
     */
    public void setDiscipline(@Valid Discipline discipline) {
        this.discipline = discipline;
    }

    /**
     * Gets the timeslots of the session.
     * @return The timeslots of the session.
     */
    public Set<@Valid Timeslot> getTimeslots() {
        return timeslots;
    }

    /**
     * Sets the timeslots of the session.
     * @param timeslots The timeslots of the session.
     */
    public void setTimeslots(
            @NotEmpty(message = "Timeslots must not be empty")
            Set<@Valid Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    /**
     * Gets the teachers of the session.
     * @return The teachers of the session.
     */
    public Set<@Valid Teacher> getTeachers() {
        return teachers;
    }

    /**
     * Sets the teachers of the session.
     * @param teachers The teachers of the session.
     */
    public void setTeachers(
            @NotEmpty(message = "Teachers must not be empty")
            Set<@Valid Teacher> teachers) {
        this.teachers = teachers;
    }

    /**
     * Gets the student groups of the session.
     * @return The student groups of the session.
     */
    public Set<@Valid StudentGroup> getGroups() {
        return studentGroups;
    }

    /**
     * Sets the student groups of the session.
     * @param studentGroups The student groups of the session.
     */
    public void setGroups(
            @NotEmpty(message = "Student groups must not be empty")
            Set<@Valid StudentGroup> studentGroups) {
        this.studentGroups = studentGroups;
    }

    /**
     * Gets the half year of the session.
     * @return The half year of the session.
     */
    @NotBlank(message = "Half year must not be blank")
    public String getHalfYear() {
        return halfYear;
    }

    /**
     * Sets the half year of the session.
     * @param halfYear The half year of the session.
     */
    public void setHalfYear(
            @NotBlank(message = "Half year must not be blank")
            String halfYear) {
        this.halfYear = halfYear;
    }

    /**
     * Adds a timeslot to the session.
     */
    public enum Type {
        COURSE, LABORATORY, SEMINARY
    }
}
