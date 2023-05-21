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

@ValidTimeslot
@Entity(name = "Timeslot")
@Table(name = "timeslot", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Timeslot implements Serializable {

    @NotNull(message = "Session must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id", referencedColumnName = "Id")
    private Session session;

    @NotNull(message = "Room must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "Id")
    private Room room;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Basic
    @Temporal(TemporalType.TIME)
    @NotNull(message = "Time must not be null")
    @Column(name = "Time", nullable = false)
    private Date time;

    @Enumerated(EnumType.STRING)
    @Column(name = "Weekday", nullable = false)
    private Day weekday;

    @NotNull(message = "Timespan must not be null")
    @DurationMin(minutes = 30, message = "[ERROR] You can reserve a minimum of 30 minutes.")
    @DurationMax(minutes = 240, message = "[ERROR] You can reserve a maximum of 240 minutes.")
    @Column(name = "Timespan", nullable = false)
    private java.time.Duration timespan;

    @Enumerated(EnumType.STRING)
    @Column(name = "Periodicity", nullable = false)
    private Periodicity periodicity;

    @NotNull(message = "Start date must not be null")
    @Column(name = "StartDate", nullable = false)
    private Date startDate;

    @NotNull(message = "End date must not be null")
    @Column(name = "EndDate", nullable = false)
    private Date endDate;

    @NotNull(message = "Insert time must not be null")
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    @Valid
    public Room getRoom() {
        return room;
    }

    public void setRoom(@Valid Room room) {
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull(message = "Time must not be null")
    public Date getTime() {
        return time;
    }

    public void setTime(
            @NotNull(message = "Time must not be null")
            Date time) {
        this.time = time;
    }

    public Day getWeekday() {
        return weekday;
    }

    public void setWeekday(Day weekday) {
        this.weekday = weekday;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
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

    @Valid
    public Session getSession() {
        return session;
    }

    public void setSession(@Valid Session session) {
        this.session = session;
    }

    @NotNull(message = "Timespan must not be null")
    public Duration getTimespan() {
        return timespan;
    }

    public void setTimespan(
            @NotNull(message = "Timespan must not be null")
            Duration timespan) {
        this.timespan = timespan;
    }

    @NotNull(message = "Start date must not be null")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(
            @NotNull(message = "Start date must not be null")
            Date startDate) {
        this.startDate = startDate;
    }

    @NotNull(message = "End date must not be null")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(
            @NotNull(message = "End date must not be null")
            Date endDate) {
        this.endDate = endDate;
    }

    public enum Periodicity {
        WEEKLY, BIWEEKLY, MONTHLY
    }

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
