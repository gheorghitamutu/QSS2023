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

@ValidSession
@Entity(name = "Session")
@Table(name = "session", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Session implements Serializable {

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "discipline_id", referencedColumnName = "Id")
    private Discipline discipline;

    @ManyToMany(mappedBy = "sessions", cascade=CascadeType.ALL)
    private Set<@Valid StudentGroup> studentGroups = new HashSet<>();

    @ManyToMany(mappedBy = "sessions", cascade=CascadeType.ALL)
    private Set<@Valid Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "session", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Timeslot> timeslots = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must not be null")
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    @Length(min = 1, max = 1)
    @Pattern(regexp = "[A-Z]{1}")
    @NotBlank(message = "Half year must not be blank")
    @Column(name = "HalfYear")
    private String halfYear;

    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Session(){
    }

    public Session(Type type,
                   @NotBlank(message = "Half year must not be blank")
                   String halfYear){
        this.type = type;
        this.halfYear = halfYear;
    }

    @NotNull(message = "Id must not be null")
    public int getId() {
        return id;
    }

    public void setId(
            @NotNull(message = "Id must not be null")
            int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @NotNull(message = "Half year must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    @Valid
    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(@Valid Discipline discipline) {
        this.discipline = discipline;
    }

    public Set<@Valid Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(
            @NotEmpty(message = "Timeslots must not be empty")
            Set<@Valid Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public Set<@Valid Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(
            @NotEmpty(message = "Teachers must not be empty")
            Set<@Valid Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<@Valid StudentGroup> getGroups() {
        return studentGroups;
    }

    public void setGroups(
            @NotEmpty(message = "Student groups must not be empty")
            Set<@Valid StudentGroup> studentGroups) {
        this.studentGroups = studentGroups;
    }

    @NotBlank(message = "Half year must not be blank")
    public String getHalfYear() {
        return halfYear;
    }

    public void setHalfYear(
            @NotBlank(message = "Half year must not be blank")
            String halfYear) {
        this.halfYear = halfYear;
    }

    public enum Type {
        COURSE, LABORATORY, SEMINARY
    }
}
