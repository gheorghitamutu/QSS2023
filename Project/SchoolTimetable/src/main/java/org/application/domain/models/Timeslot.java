package org.application.domain.models;

import jakarta.persistence.*;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.application.domain.models.validators.timeslot.ValidTimeslot;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

@ValidTimeslot
@Entity(name = "Timeslot")
@Table(name = "timeslot", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Timeslot implements Serializable {
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id", referencedColumnName = "Id")
    private Session session;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "Id")
    private Room room;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;
    @Basic
    @Temporal(TemporalType.TIME)
    @Column(name = "Time", nullable = false)
    private Date time;
    @Enumerated(EnumType.STRING)
    @Column(name = "Weekday", nullable = false)
    private Day weekday;
    @DurationMin(minutes = 30, message = "[ERROR] You can reserve a minimum of 30 minutes.")
    @DurationMax(minutes = 240, message = "[ERROR] You can reserve a maximum of 240 minutes.")
    @Column(name = "Timespan", nullable = false)
    private java.time.Duration timespan;
    @Enumerated(EnumType.STRING)
    @Column(name = "Periodicity", nullable = false)
    private Periodicity periodicity;
    @Column(name = "StartDate", nullable = false)
    private Date startDate;
    @Column(name = "EndDate", nullable = false)
    private Date endDate;
    @Column(name = "insert_time", nullable = false)
    private Date insertTime;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(@Valid Session session) {
        this.session = session;
    }

    public Duration getTimespan() {
        return timespan;
    }

    public void setTimespan(Duration timespan) {
        this.timespan = timespan;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public enum Periodicity {
        WEEKLY, BIWEEKLY, MONTHLY
    }

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
