package org.application.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Room")
@Table(name = "room", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@NamedQuery(name = "Room.getByName", query = "SELECT r FROM Room r WHERE r.name = :name")
public class Room implements Serializable {

    @OneToMany(mappedBy = "room")
    private Set<Timeslot> timeslots = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Capacity", nullable = false, unique=true)
    private int capacity;

    @Column(name = "Floor", nullable = false)
    private int floor;

    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Set<@Valid Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public enum Type {
        COURSE, LABORATORY
    }
}
