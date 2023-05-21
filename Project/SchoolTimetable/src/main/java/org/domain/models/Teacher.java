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

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Teacher_Session", joinColumns = {@JoinColumn(name = "teacher_id")}, inverseJoinColumns = {@JoinColumn(name = "session_id")})
    private Set<@Valid Session> sessions = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Teacher_Discipline", joinColumns = {@JoinColumn(name = "teacher_id")}, inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    private Set<@Valid Discipline> disciplines = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false, unique=true)
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    @Column(name = "insert_time", nullable = false)
    @NotNull(message = "Insert time must not be null")
    private Date insertTime;

    public Teacher() {
    }

    public Teacher(
            @NotBlank(message = "Name must not be blank")
            String name, Type type){
        this.name = name;
        this.type = type;
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

    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    public Set<@Valid Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<@Valid Session> sessions) {
        this.sessions = sessions;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<@Valid Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(
            @NotEmpty(message = "Disciplines must not be empty")
            Set<@Valid Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public enum Type {
        TEACHER, COLLABORATOR
    }
}
