package org.application.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.application.domain.models.validators.teacher.ValidTeacher;

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
    private Set<Session> sessions = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Teacher_Discipline", joinColumns = {@JoinColumn(name = "teacher_id")}, inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    private Set<Discipline> disciplines = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

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


    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Set<Session> getSessions() {
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

    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<@Valid Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public enum Type {
        TEACHER, COLLABORATOR
    }
}
