package org.domain.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Room")
@Table(name = "room", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@NamedQuery(name = "Room.getByName", query = "SELECT r FROM Room r WHERE r.name = :name")
public class Room implements Serializable {

    @OneToMany(mappedBy = "room", orphanRemoval = true)
    private Set<@Valid Timeslot> timeslots = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must not be null")
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false, unique = true)
    private String name;

    @Min(value = 1, message = "Capacity must be greater than 0")
    @Column(name = "Capacity", nullable = false)
    private int capacity;

    @Min(value = 0, message = "Floor must be greater than or equal to 0")
    @Column(name = "Floor", nullable = false)
    private int floor;

    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Room() {
    }

    public Room(
            @NotBlank(message = "Name must not be blank")
            String name,
            Type type,
            @Min(value = 1, message = "Capacity must be greater than 0")
            int capacity,
            @Min(value = 0, message = "Floor must be greater than or equal to 0")
            int floor) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.floor = floor;
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

    @NotBlank(message = "Name must not be blank")
    public String getName() {
        return name;
    }

    public void setName(
            @NotBlank(message = "Name must not be blank")
            String name) {
        this.name = name;
    }

    @Min(value = 1, message = "Capacity must be greater than 0")
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(
            @Min(value = 1, message = "Capacity must be greater than 0")
            int capacity) {
        this.capacity = capacity;
    }

    @Min(value = 0, message = "Floor must be greater than or equal to 0")
    public int getFloor() {
        return floor;
    }

    public void setFloor(
            @Min(value = 0, message = "Floor must be greater than or equal to 0")
            int floor) {
        this.floor = floor;
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

    public Set<@Valid Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(
            @NotEmpty(message = "Timeslots must not be empty")
            Set<@Valid Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public enum Type {
        COURSE, LABORATORY
    }
}
