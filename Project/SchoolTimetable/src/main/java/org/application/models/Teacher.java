package org.application.models;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Teacher")
@Table(name = "teacher", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Teacher implements Serializable {

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Teacher_Session",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name = "session_id")})
    private Set<Session> sessions = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Type", nullable = false)
    private String type;

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

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
