package com.trains.tickets.graph;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private int distance; // текущая дистанция от начальной вершины
    private List<Integer> parentVertices; // текущий родитель вершины

    public Path(int distance) {
        this.distance = distance;
        this.parentVertices = new ArrayList<>();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Integer> getParentVertices() {
        return parentVertices;
    }

    public void setParentVertices(List<Integer> parentVertices) {
        this.parentVertices = parentVertices;
    }
}
