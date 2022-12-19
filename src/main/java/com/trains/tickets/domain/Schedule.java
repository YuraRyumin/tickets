package com.trains.tickets.domain;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String time;
    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_train")
    private Train train;

    public Schedule() {
    }

    public Schedule(String time, Integer dayOfWeek, Train train) {
        this.time = time;
        this.dayOfWeek = dayOfWeek;
        this.train = train;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Schedule other = (Schedule) obj;
        if ((this.time == null) ? (other.time != null) : !this.time.equals(other.time)) {
            return false;
        }
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.train == null) ? (other.train != null) : !this.train.equals(other.train)) {
            return false;
        }
        if ((this.dayOfWeek == null) ? (other.dayOfWeek != null) : !this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = 53 * hash + this.id + this.dayOfWeek + this.train.hashCode();
        return hash;
    }
}
