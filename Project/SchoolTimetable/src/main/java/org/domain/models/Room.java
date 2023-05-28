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

/**
 * This class represents a room.
 */
@Entity(name = "Room")
@Table(name = "room", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@NamedQuery(name = "Room.getByName", query = "SELECT r FROM Room r WHERE r.name = :name")
public class Room implements Serializable {

    /**
     * The timeslots that the room is booked.
     * It is a set of timeslots.
     * It is a one to many relationship.
     * It is a bidirectional relationship.
     * It is a set of valid timeslots.
     */
    @OneToMany(mappedBy = "room", orphanRemoval = true)
    private Set<@Valid Timeslot> timeslots = new HashSet<>();

    /**
     * The id of the room.
     * It is a primary key.
     * It is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    /**
     * The type of the room.
     * It is not null.
     * It is a string.
     * It is an enumerated type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type type;

    /**
     * The name of the room.
     * It is unique.
     * It is not null.
     * It is a string.
     */
    @NotBlank(message = "Name must not be blank")
    @Column(name = "Name", nullable = false, unique = true)
    private String name;

    /**
     * The capacity of the room.
     * It is not null.
     * It is an integer.
     * It is greater than 0.
     */
    @Min(value = 1, message = "Capacity must be greater than 0")
    @Column(name = "Capacity", nullable = false)
    private int capacity;

    /**
     * The floor of the room.
     * It is not null.
     * It is an integer.
     * It is greater than or equal to 0.
     */
    @Min(value = 0, message = "Floor must be greater than or equal to 0")
    @Column(name = "Floor", nullable = false)
    private int floor;

    /**
     * The insert time of the room.
     * It is not null.
     * It is a date.
     */
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    /**
     * The default constructor of the room.
     * It initializes the insert time with the current date.
     */
    public Room() {
        this.insertTime = new Date();
    }

    /**
     * The constructor of the room.
     * It initializes the name, type, capacity, floor and insert time.
     *
     * @param name     The name of the room.
     * @param type     The type of the room.
     * @param capacity The capacity of the room.
     * @param floor    The floor of the room.
     */
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

    /**
     * Gets the id of the room.
     * @return The id of the room.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the room.
     * @param id The id of the room.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the type of the room.
     * @return The type of the room.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the room.
     * @param type The type of the room.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets the name of the room.
     * @return The name of the room.
     * @throws IllegalArgumentException If the name is blank.
     */
    @NotBlank(message = "Name must not be blank")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the room.
     * @param name The name of the room.
     * @throws IllegalArgumentException If the name is blank.
     */
    public void setName(
            @NotBlank(message = "Name must not be blank")
            String name) {
        this.name = name;
    }

    /**
     * Gets the capacity of the room.
     * @return The capacity of the room.
     * @throws IllegalArgumentException If the capacity is less than 1.
     */
    @Min(value = 1, message = "Capacity must be greater than 0")
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the room.
     * @param capacity The capacity of the room.
     * @throws IllegalArgumentException If the capacity is less than 1.
     */
    public void setCapacity(
            @Min(value = 1, message = "Capacity must be greater than 0")
            int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the floor of the room.
     * @return The floor of the room.
     * @throws IllegalArgumentException If the floor is less than 0.
     */
    @Min(value = 0, message = "Floor must be greater than or equal to 0")
    public int getFloor() {
        return floor;
    }

    /**
     * Sets the floor of the room.
     * @param floor The floor of the room.
     * @throws IllegalArgumentException If the floor is less than 0.
     */
    public void setFloor(
            @Min(value = 0, message = "Floor must be greater than or equal to 0")
            int floor) {
        this.floor = floor;
    }

    /**
     * Gets the insert time of the room.
     * @return The insert time of the room.
     * @throws IllegalArgumentException If the insert time is null.
     */
    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * Sets the insert time of the room.
     * @param insertTime The insert time of the room.
     * @throws IllegalArgumentException If the insert time is null.
     */
    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * Gets the timeslots of the room.
     * @return The timeslots of the room.
     */
    public Set<@Valid Timeslot> getTimeslots() {
        return timeslots;
    }

    /**
     * Sets the timeslots of the room.
     * @param timeslots The timeslots of the room.
     */
    public void setTimeslots(
            @NotEmpty(message = "Timeslots must not be empty")
            Set<@Valid Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    /**
     * The type of the room.
     */
    public enum Type {
        COURSE, LABORATORY
    }
}
