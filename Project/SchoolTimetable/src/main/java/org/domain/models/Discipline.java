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

@Entity(name = "Discipline")
@Table(name = "discipline", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@NamedQuery(name = "Discipline.getByName", query = "SELECT d FROM Discipline d WHERE d.name = :name")
public class Discipline implements Serializable {

    @ManyToMany(mappedBy = "disciplines", cascade = CascadeType.ALL)
    private Set<@Valid Student> students = new HashSet<>();

    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Session> sessions = new HashSet<>();

    @ManyToMany(mappedBy = "disciplines", cascade=CascadeType.ALL)
    private Set<@Valid Teacher> teachers = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must not be null")
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false, unique=true)
    private String name;

    @Positive(message = "Credits must be positive")
    @Column(name = "Credits", nullable = false)
    private int credits;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Discipline() {
    }

    public Discipline(
            @NotBlank(message = "Name must not be blank")
            String name,
            @Positive(message = "Credits must be positive")
            int credits){
        this.name = name;
        this.credits = credits;
    }

    @NotNull(message = "Id must not be null")
    public int getId() {
        return id;
    }

    public void setId(
            @NotNull(message = "Id must not be null")
            int id
    ) {
        this.id = id;
    }

    @NotBlank(message = "Name must not be blank")
    public String getName() {
        return name;
    }

    public void setName(
            @NotBlank(message = "Name must not be blank")
            String name
    ) {
        this.name = name;
    }

    @Positive(message = "Credits must be positive")
    public int getCredits() {
        return credits;
    }

    public void setCredits(
            @Positive(message = "Credits must be positive")
            int credits
    ) {
        this.credits = credits;
    }

    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime
    ) {
        this.insertTime = insertTime;
    }

    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    @Valid
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<
            @Valid
            @NotEmpty(message = "Students must not be empty")
                    Student> students
    ) {
        this.students = students;
    }

    @Valid
    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<
            @Valid
            @NotEmpty(message = "Sessions must not be empty")
                    Session> sessions) {
        this.sessions = sessions;
    }

    @Valid
    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<
            @Valid @NotEmpty(message = "Teachers must not be empty")
                    Teacher> teachers) {
        this.teachers = teachers;
    }
}
