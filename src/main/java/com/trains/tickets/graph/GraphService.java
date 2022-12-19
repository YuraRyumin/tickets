package com.trains.tickets.graph;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.repository.DistanceRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.StopRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GraphService {
    private final StationRepository stationRepository;
    private final DistanceRepository distanceRepository;
    private final StopRepository stopRepository;

    public GraphService(StationRepository stationRepository, DistanceRepository distanceRepository, StopRepository stopRepository) {
        this.stationRepository = stationRepository;
        this.distanceRepository = distanceRepository;
        this.stopRepository = stopRepository;
    }

    public Graph getFilledGraph(){
        Graph graph = new Graph();
        Station stationFirstObj = null;
        Station stationLastObj = null;
        List<Station> stations = new ArrayList<>();
        List<Distance> distances = distanceRepository.findAll();
        for(Distance distance: distances){
            stationFirstObj = distance.getStationFirst();
            stationLastObj = distance.getStationLast();
            if(!stationFirstObj.equals(null) && stations.indexOf(stationFirstObj) == -1){
                stations.add(stationFirstObj);
            }
            if(!stationLastObj.equals(null) && stations.indexOf(stationLastObj) == -1){
                stations.add(stationLastObj);
            }
        }
        for(Station station: stations){
            List<Schedule> scheduleList = new ArrayList<>();
            Set<Stop> stopSet = stopRepository.findAllByStation(station);
            for(Stop stop: stopSet){
                scheduleList.add(stop.getSchedule());
            }
            graph.addVertex(station, scheduleList);
        }

        for(Distance distance: distances){
            Integer stationFirst = stations.indexOf(distance.getStationFirst());
            Integer stationLast = stations.indexOf(distance.getStationLast());
            graph.addEdge(stationFirst,
                    stationLast,
                    distance.getKilometers());
            graph.addEdge(stationLast,
                    stationFirst,
                    distance.getKilometers());
        }

        return graph;
    }
}
