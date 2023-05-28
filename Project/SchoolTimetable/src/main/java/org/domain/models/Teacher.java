package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.domain.models.validators.teacher.ValidTeacher;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ValidTeacher
@Entity(name = "Teacher")
@Table(name = "teacher", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@NamedQuery(name = "Teacher.getByName", query = "SELECT t FROM Teacher t WHERE t.name = :name")
public class Teacher implements Serializable {

    /**
     * The sessions that the teacher is teaching.
     * It is a set of sessions.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     */
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Teacher_Session", joinColumns = {@JoinColumn(name = "teacher_id")}, inverseJoinColumns = {@JoinColumn(name = "session_id")})
    private Set<@Valid Session> sessions = new HashSet<>();

    /**
     * The disciplines that the teacher is teaching.
     * It is a set of disciplines.
     * It is a many to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid disciplines.
     */
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Teacher_Discipline", joinColumns = {@JoinColumn(name = "teacher_id")}, inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    private Set<@Valid Discipline> disciplines = new HashSet<>();

    /**
     * The id of the teacher.
     * It is generated automatically.
     * It is unique.
     * It is not null.
     * It is a primary key.
     * It is an integer.
     * It is not updatable.
     * It is not insertable.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    /**
     * The name of the teacher.
     */
    @Column(name = "Name", nullable = false, unique=true)
    @NotBlank(message = "Name must not be blank")
    private String name;

    /**
     * The type of the teacher.
     * It is an enumeration.
     * It is not null.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    /**
     * The insert time of the teacher.
     * It is not null.
     * It is a date.
     */
    @Column(name = "insert_time", nullable = false)
    @NotNull(message = "Insert time must not be null")
    private Date insertTime;

    /**
     * Represents a teacher instance.
     * It is a default constructor.
     */
    public Teacher() {
        this.insertTime = new Date();
    }

    /**
     * Represents a teacher instance.
     * @param name The name of the teacher.
     * @param type The type of the teacher.
     */
    public Teacher(
            @NotBlank(message = "Name must not be blank")
            String name, Type type){
        this.name = name;
        this.type = type;
        this.insertTime = new Date();
    }

    /**
     * Gets the id of the teacher.
     * @return The id of the teacher.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the teacher.
     * @param id The id of the teacher.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the teacher.
     * @return The name of the teacher.
     * @throws IllegalArgumentException If the name is blank.
     */
    @NotBlank(message = "Name must not be blank")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the teacher.
     * @param name The name of the teacher.
     * @throws IllegalArgumentException If the name is blank.
     */
    public void setName(
            @NotBlank(message = "Name must not be blank")
            String name) {
        this.name = name;
    }

    /**
     * Gets the insert time of the teacher.
     * @return The insert time of the teacher.
     * @throws IllegalArgumentException If the insert time is null.
     */
    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * Sets the insert time of the teacher.
     * @param insertTime The insert time of the teacher.
     * @throws IllegalArgumentException If the insert time is null.
     */
    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * Gets the sessions that the teacher is teaching.
     * @return The sessions that the teacher is teaching.
     */
    public Set<@Valid Session> getSessions() {
        return sessions;
    }

    /**
     * Sets the sessions that the teacher is teaching.
     * @param sessions The sessions that the teacher is teaching.
     * @throws IllegalArgumentException If the sessions are null.
     */
    public void setSessions(Set<@Valid Session> sessions) {
        this.sessions = sessions;
    }

    /**
     * Gets the type of the teacher.
     * @return The type of the teacher.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the teacher.
     * @param type The type of the teacher.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets the disciplines that the teacher is teaching.
     * @return The disciplines that the teacher is teaching.
     */
    public Set<@Valid Discipline> getDisciplines() {
        return disciplines;
    }

    /**
     * Sets the disciplines that the teacher is teaching.
     * @param disciplines The disciplines that the teacher is teaching.
     * @throws IllegalArgumentException If the disciplines are empty.
     */
    public void setDisciplines(
            @NotEmpty(message = "Disciplines must not be empty")
            Set<@Valid Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    /**
     * Represents the type of the teacher.
     */
    public enum Type {
        TEACHER, COLLABORATOR
    }
}
