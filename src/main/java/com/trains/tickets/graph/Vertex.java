package com.trains.tickets.graph;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private Station station;
    private List<Schedule> scheduleList;
    private boolean isInTree;

    public Vertex(Station station) {
        this.station = station;
        this.isInTree = false;
        this.scheduleList = new ArrayList<>();
    }

    public Vertex(Station station, List<Schedule> scheduleList) {
        this.station = station;
        this.isInTree = false;
        this.scheduleList = scheduleList;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public boolean isInTree() {
        return isInTree;
    }

    public void setInTree(boolean inTree) {
        isInTree = inTree;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
