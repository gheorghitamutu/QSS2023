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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Student> students = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "StudentGroup_Session", joinColumns = {@JoinColumn(name = "studentgroup_id", referencedColumnName = "Id")}, inverseJoinColumns = {@JoinColumn(name = "session_id", referencedColumnName = "Id")})
    private Set<@Valid Session> sessions = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Length(min = 2, max = 2)
    @Pattern(regexp = "[A-Z]{1}[0-9]{1}")
    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false)
    private String name;

    @Min(value = 1, message = "Year must be greater than 0")
    @Max(value = 3, message = "Year must be less than 4")
    @Column(name = "\"Year\"", nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private StudentGroup.Type type;

    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public StudentGroup() {
    }

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

    public Set<@Valid Student> getStudents() {
        return students;
    }

    public void setStudents(
            @NotEmpty(message = "Students must not be empty")
            Set<@Valid Student> students) {
        this.students = students;
    }

    @NotEmpty(message = "Name must not be empty")
    public String getName() {
        return name;
    }

    public void setName(
            @NotEmpty(message = "Name must not be empty")
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

    public void setSessions(
            @NotEmpty(message = "Sessions must not be empty")
            Set<@Valid Session> sessions) {
        this.sessions = sessions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        BACHELOR, MASTER
    }
}