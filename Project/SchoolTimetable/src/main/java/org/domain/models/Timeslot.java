package org.domain.models;

import jakarta.persistence.*;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.domain.models.validators.timeslot.ValidTimeslot;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

/**
 * This class represents a timeslot.
 */
@ValidTimeslot
@Entity(name = "Timeslot")
@Table(name = "timeslot", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Timeslot implements Serializable {

    /**
     * The session of the timeslot.
     */
    @NotNull(message = "Session must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id", referencedColumnName = "Id")
    private Session session;

    /**
     * The room of the timeslot.
     */
    @NotNull(message = "Room must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "Id")
    private Room room;

    /**
     * The id of the timeslot.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    /**
     * The time of the timeslot.
     */
    @Basic
    @Temporal(TemporalType.TIME)
    @NotNull(message = "Time must not be null")
    @Column(name = "Time", nullable = false)
    private Date time;

    /**
     * The weekday of the timeslot.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Weekday", nullable = false)
    private Day weekday;

    /**
     * The timespan of the timeslot.
     */
    @NotNull(message = "Timespan must not be null")
    @DurationMin(minutes = 30, message = "[ERROR] You can reserve a minimum of 30 minutes.")
    @DurationMax(minutes = 240, message = "[ERROR] You can reserve a maximum of 240 minutes.")
    @Column(name = "Timespan", nullable = false)
    private java.time.Duration timespan;

    /**
     * The periodicity of the timeslot.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Periodicity", nullable = false)
    private Periodicity periodicity;

    /**
     * The start date of the timeslot.
     */
    @NotNull(message = "Start date must not be null")
    @Column(name = "StartDate", nullable = false)
    private Date startDate;

    /**
     * The end date of the timeslot.
     */
    @NotNull(message = "End date must not be null")
    @Column(name = "EndDate", nullable = false)
    private Date endDate;

    /**
     * The insert time of the timeslot.
     */
    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    /**
     * Gets the room of the timeslot.
     * @return The room of the timeslot.
     */
    @Valid
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the room of the timeslot.
     * @param room The room of the timeslot.
     */
    public void setRoom(@Valid Room room) {
        this.room = room;
    }

    /**
     * Gets the timespan of the timeslot.
     * @return The timespan of the timeslot.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the timeslot.
     * @param id The id of the timeslot.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the time of the timeslot.
     * @return The time of the timeslot.
     * @throws IllegalArgumentException If the time is null.
     */
    @NotNull(message = "Time must not be null")
    public Date getTime() {
        return time;
    }

    /**
     * Sets the time of the timeslot.
     * @param time The time of the timeslot.
     * @throws IllegalArgumentException If the time is null.
     */
    public void setTime(
            @NotNull(message = "Time must not be null")
            Date time) {
        this.time = time;
    }

    /**
     * Gets the weekday of the timeslot.
     * @return The weekday of the timeslot.
     */
    public Day getWeekday() {
        return weekday;
    }

    /**
     * Sets the weekday of the timeslot.
     * @param weekday The weekday of the timeslot.
     */
    public void setWeekday(Day weekday) {
        this.weekday = weekday;
    }

    /**
     * Gets the periodicity of the timeslot.
     * @return The periodicity of the timeslot.
     */
    public Periodicity getPeriodicity() {
        return periodicity;
    }

    /**
     * Sets the periodicity of the timeslot.
     * @param periodicity The periodicity of the timeslot.
     */
    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    /**
    * Gets the insert time of the timeslot.
    * @return The insert time of the timeslot.
    * @throws IllegalArgumentException If the insert time is null.
     */
    @NotNull(message = "Insert time must not be null")
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * Sets the insert time of the timeslot.
     * @param insertTime The insert time of the timeslot.
     * @throws IllegalArgumentException If the insert time is null.
     */
    public void setInsertTime(
            @NotNull(message = "Insert time must not be null")
            Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * Gets the session of the timeslot.
     * @return The session of the timeslot.
     */
    @Valid
    public Session getSession() {
        return session;
    }

    /**
    * Sets the session of the timeslot.
    * @param session The session of the timeslot.
     */
    public void setSession(@Valid Session session) {
        this.session = session;
    }

    /**
     * Gets the duration of the timeslot.
     * @return The duration of the timeslot.
     */
    @NotNull(message = "Timespan must not be null")
    public Duration getTimespan() {
        return timespan;
    }

    /**
     * Sets the duration of the timeslot.
     * @param timespan The duration of the timeslot.
     */
    public void setTimespan(
            @NotNull(message = "Timespan must not be null")
            Duration timespan) {
        this.timespan = timespan;
    }

    /**
     * Gets the start date of the timeslot.
     * @return The start date of the timeslot.
     */
    @NotNull(message = "Start date must not be null")
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the timeslot.
     * @param startDate The start date of the timeslot.
     */
    public void setStartDate(
            @NotNull(message = "Start date must not be null")
            Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the timeslot.
     * @return The end date of the timeslot.
     */
    @NotNull(message = "End date must not be null")
    public Date getEndDate() {
        return endDate;
    }

    /**
    * Sets the end date of the timeslot.
    * @param endDate The end date of the timeslot.
     */
    public void setEndDate(
            @NotNull(message = "End date must not be null")
            Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Periodicity of the timeslot.
     */
    public enum Periodicity {
        WEEKLY, BIWEEKLY, MONTHLY
    }

    /**
     * The order of the days is important for the periodicity of the timeslot.
     * The first day of the week is Monday, the last is Sunday.
     */
    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
