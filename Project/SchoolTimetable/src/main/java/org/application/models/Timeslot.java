package org.application.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "Timeslot")
@Table(name = "timeslot", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
public class Timeslot implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id", referencedColumnName = "Id")
    private Session session;
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
    @Min(30)
    @Column(name = "Timespan", nullable = false)
    private int timespan;
    @Enumerated(EnumType.STRING)
    @Column(name = "Periodicity", nullable = false)
    private Periodicity periodicity;
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

    public void setSession(Session session) {
        this.session = session;
    }

    public int getTimespan() {
        return timespan;
    }

    public void setTimespan(int timespan) {
        this.timespan = timespan;
    }

    public enum Periodicity {
        WEEKLY, BIWEEKLY, MONTHLY
    }

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
