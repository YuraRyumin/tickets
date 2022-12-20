package com.trains.tickets.graph;

import com.trains.tickets.domain.Schedule;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.Stop;
import com.trains.tickets.dto.ManyTrainsTripDTO;
import com.trains.tickets.dto.OneTrainTripDTO;

import java.time.LocalTime;
import java.util.*;

public class Graph {
    private final int MAX_VERTS = 50;
    private final int INFINITY = 100000000;
    private Vertex vertexList[];
    private int relationMatrix[][];
    private int countOfVertices;
    private int countOfVertexInTree;
    private List<Path> shortestPaths;
    private int currentVertex;
    private int startToCurrent;

    public Graph() {
        vertexList = new Vertex[MAX_VERTS];
        relationMatrix = new int[MAX_VERTS][MAX_VERTS];
        countOfVertices = 0;
        countOfVertexInTree = 0;
        for (int i = 0; i < MAX_VERTS; i++) {
            for (int k = 0; k < MAX_VERTS; k++) {
                relationMatrix[i][k] = INFINITY;
                shortestPaths = new ArrayList<>();
            }
        }
    }

    public void addVertex(Station station) {
        vertexList[countOfVertices++] = new Vertex(station);
    }

    public void addVertex(Station station, List<Schedule> scheduleList) {
        vertexList[countOfVertices++] = new Vertex(station, scheduleList);
    }

    public void addEdge(int start, int end, int weight) {
        relationMatrix[start][end] = weight;
    }

    public void path() {
        int startTree = 0;

        for(Integer i = 0; i < vertexList.length; i++){
            if(vertexList[i] == null){
                break;
            }
            if(vertexList[i].getStation().getName().equals("L1-4")){
                startTree = i;
            }
        }
        System.out.println(startTree);

        vertexList[startTree].setInTree(true);
        countOfVertexInTree = 1;

        for (int i = 0; i < countOfVertices; i++) {
            int tempDist = relationMatrix[startTree][i];
            Path path = new Path(tempDist);
            path.getParentVertices().add(startTree);
            shortestPaths.add(path);
        }
        while (countOfVertexInTree < countOfVertices) {
            int indexMin = getMin();
            int minDist = shortestPaths.get(indexMin).getDistance();

            if (minDist == INFINITY) {
                System.out.println("В графе пристувствуют недостижимые вершины");
                break;
            } else {
                currentVertex = indexMin;
                startToCurrent = shortestPaths.get(indexMin).getDistance();
            }

            vertexList[currentVertex].setInTree(true);
            countOfVertexInTree++;
            updateShortestPaths();
        }

        displayPaths();
    }

    public Set<ManyTrainsTripDTO> findWaysBetweenTwoStations(Station stationFirst,
                                                               Station stationLast,
                                                               List<Stop> stopList,
                                                               String dateTicket){
        Set<OneTrainTripDTO> oneTrainTripDTOS = new HashSet<>();
        Set<ManyTrainsTripDTO> manyTrainsTripDTOSet = new HashSet<>();
        int startTree = 0;
        int endTree = 0;

        for(Integer i = 0; i < vertexList.length; i++){
            if(vertexList[i] == null){
                break;
            }
            if(vertexList[i].getStation().equals(stationFirst)){
                startTree = i;
            }
            if(vertexList[i].getStation().equals(stationLast)){
                endTree = i;
            }
        }

        vertexList[startTree].setInTree(true);
        countOfVertexInTree = 1;

        for (int i = 0; i < countOfVertices; i++) {
            int tempDist = relationMatrix[startTree][i];
            Path path = new Path(tempDist);
            path.getParentVertices().add(startTree);
            shortestPaths.add(path);
        }
        while (countOfVertexInTree < countOfVertices) {
            int indexMin = getMin();
            int minDist = shortestPaths.get(indexMin).getDistance();

            if (minDist == INFINITY) {
                System.out.println("The graph has unreachable vertices");
                break;
            } else {
                currentVertex = indexMin;
                startToCurrent = shortestPaths.get(indexMin).getDistance();
            }

            vertexList[currentVertex].setInTree(true);
            countOfVertexInTree++;
            updateShortestPaths();
        }

        if (shortestPaths.get(endTree).getDistance() == INFINITY) {

        } else {
            String result = shortestPaths.get(endTree).getDistance() + " (";
            List<Integer> parents = shortestPaths.get(endTree).getParentVertices();
            String scheduleResult = "";

            for (int j = 0; j < parents.size(); j++) {
                result += vertexList[parents.get(j)].getStation().getName() + "(";
                scheduleResult = "";
                for(Schedule schedule: vertexList[parents.get(j)].getScheduleList()){
                    if(scheduleResult.equals("")){
                        scheduleResult += schedule.getTime();
                    } else {
                        scheduleResult += ";" + schedule.getTime();
                    }
                }
                result += scheduleResult + ") -> ";
            }

            String[] fullDate = dateTicket.split("-");
            Integer dayOfDate = Integer.valueOf(fullDate[2]);
            Integer monthOfDate = Integer.valueOf(fullDate[1]);
            Integer yearOfDate = Integer.valueOf(fullDate[0]);
            Calendar calendar = new GregorianCalendar(yearOfDate, monthOfDate - 1 , dayOfDate);

            cycleOfStations(parents, manyTrainsTripDTOSet, endTree, stopList, calendar.get(Calendar.DAY_OF_WEEK));
            System.out.println(yearOfDate + "; " + monthOfDate + "; " + dayOfDate);
            result += vertexList[endTree].getStation().getName() + "(";
            scheduleResult = "";

            for(Schedule schedule: vertexList[endTree].getScheduleList()){
                if(scheduleResult.equals("")){
                    scheduleResult += schedule.getTime();
                } else {
                    scheduleResult += ";" + schedule.getTime();
                }
            }
            result += scheduleResult + "))";
            System.out.println(result);
        }

        return manyTrainsTripDTOSet;
    }

    private void cycleOfStations(List<Integer> parents,
                                 Set<ManyTrainsTripDTO> manyTrainsTripDTOSet,
                                 Integer endTree,
                                 List<Stop> stopList,
                                 Integer dayOfWeek){
        Set<OneTrainTripDTO> oneTrainTripDTOS = new HashSet<>();
        ManyTrainsTripDTO manyTrainsTripDTO = new ManyTrainsTripDTO();

        Schedule prevSchedule = mainCycle(parents,
                                endTree,
                                0,
                                new OneTrainTripDTO(),
                                stopList,
                                manyTrainsTripDTO,
                                manyTrainsTripDTOSet,
                                oneTrainTripDTOS,
                                null,
                                dayOfWeek);
    }

    private OneTrainTripDTO cycleOfSchedules(Set<ManyTrainsTripDTO> manyTrainsTripDTOSet,
                                  Vertex vertex,
                                  Integer endTree,
                                  LocalTime localTimeOfBegining,
                                  LocalTime localTimeOfEnd,
                                  Schedule prevSchedule,
                                  List<Stop> stopList){
        OneTrainTripDTO oneTrainTripDTO = null;
        for (Schedule schedule : vertex.getScheduleList()) {
            if (schedule.equals(prevSchedule) || vertexList[endTree].getScheduleList().indexOf(schedule) == -1) {
                continue;
            }
            oneTrainTripDTO = new OneTrainTripDTO();
            localTimeOfBegining = setFirstValuesToOneTrainTripDTO(oneTrainTripDTO,
                                                                vertex.getStation(),
                                                                schedule,
                                                                stopList);
            localTimeOfEnd = setLastValuesToOneTrainTripDTO(oneTrainTripDTO,
                                                            vertexList[endTree].getStation(),
                                                            schedule,
                                                            stopList);

        }
        return oneTrainTripDTO;
    }

    private Schedule mainCycle(List<Integer> parents,
                           Integer endTree,
                           Integer numVertex,
                           OneTrainTripDTO oneTrainTripDTO,
                           List<Stop> stopList,
                           ManyTrainsTripDTO manyTrainsTripDTO,
                           Set<ManyTrainsTripDTO> manyTrainsTripDTOSet,
                           Set<OneTrainTripDTO> oneTrainTripDTOS,
                           Schedule prevSchedule,
                           Integer dayOfWeek){
        LocalTime localTimeOfBegining = null;
        LocalTime localTimeOfEnd = null;
        Vertex vertexMain = vertexList[parents.get(numVertex)];
        for(Schedule scheduleMain: vertexMain.getScheduleList()) {
            if(!scheduleMain.getDayOfWeek().equals(dayOfWeek)){
                continue;
            }
            Set<OneTrainTripDTO> oneTrainTripDTOSCopy = new HashSet<>(oneTrainTripDTOS);
            oneTrainTripDTO = new OneTrainTripDTO();

            if(parents.size() > numVertex + 1){
                if(vertexList[parents.get(numVertex + 1)].getScheduleList().indexOf(scheduleMain) == -1){
                    continue;
                }
            }

            localTimeOfBegining = setFirstValuesToOneTrainTripDTO(oneTrainTripDTO,
                                                                vertexMain.getStation(),
                                                                scheduleMain,
                                                                stopList);
            prevSchedule = scheduleMain;
            for (int i = numVertex; i < parents.size(); i++) {
                Vertex vertex = vertexList[parents.get(i)];
                if (vertex.getScheduleList().indexOf(prevSchedule) == -1) {
                    localTimeOfEnd = setLastValuesToOneTrainTripDTO(oneTrainTripDTO,
                                                                    vertexList[parents.get(i - 1)].getStation(),
                                                                    prevSchedule,
                                                                    stopList);
                    if(vertexList[parents.get(0)].equals(vertexMain)) {
                        oneTrainTripDTO.setDistance(shortestPaths.get(parents.get(i - 1)).getDistance());
                    } else {
                        Integer firstDistance = shortestPaths.get(parents.get(i - 1)).getDistance();
                        Integer secondDistance = shortestPaths.get(endTree).getDistance();
                        oneTrainTripDTO.setDistance(secondDistance - firstDistance);
                    }
                    if(localTimeOfBegining != null && localTimeOfEnd != null && localTimeOfEnd.isAfter(localTimeOfBegining)) {
                        oneTrainTripDTOSCopy.add(oneTrainTripDTO);
                        manyTrainsTripDTO.setValid(true);
                    } else {
                        manyTrainsTripDTO.setValid(false);
                    }
                    oneTrainTripDTO = new OneTrainTripDTO();
                    localTimeOfBegining = null;
                    localTimeOfEnd = null;

                    prevSchedule = mainCycle(parents,
                                            endTree,
                                            i-1,
                                            new OneTrainTripDTO(),
                                            stopList,
                                            manyTrainsTripDTO,
                                            manyTrainsTripDTOSet,
                                            oneTrainTripDTOSCopy,
                                            prevSchedule,
                                            dayOfWeek);
                    break;
                }
            }
            if (vertexList[endTree].getScheduleList().indexOf(prevSchedule) == -1) {
                localTimeOfEnd = setLastValuesToOneTrainTripDTO(oneTrainTripDTO,
                                                                vertexList[parents.get(parents.size() - 1)].getStation(),
                                                                prevSchedule,
                                                                stopList);
                if(vertexList[parents.get(0)].equals(vertexMain)) {
                    oneTrainTripDTO.setDistance(shortestPaths.get(parents.get(parents.size() - 1)).getDistance());
                } else {
                    Integer firstDistance = shortestPaths.get(parents.get(parents.size() - 1)).getDistance();
                    Integer secondDistance = shortestPaths.get(endTree).getDistance();
                    oneTrainTripDTO.setDistance(secondDistance - firstDistance);
                }
                if(localTimeOfBegining != null && localTimeOfEnd != null && localTimeOfEnd.isAfter(localTimeOfBegining)) {
                    oneTrainTripDTOSCopy.add(oneTrainTripDTO);
                    manyTrainsTripDTO.setValid(true);
                } else {
                    manyTrainsTripDTO.setValid(false);
                }
                oneTrainTripDTO = new OneTrainTripDTO();
                localTimeOfBegining = null;
                localTimeOfEnd = null;
                Vertex vertex = vertexList[parents.get(parents.size() - 1)];
                for (Schedule schedule : vertex.getScheduleList()) {
                    if (schedule.equals(prevSchedule) || vertexList[endTree].getScheduleList().indexOf(schedule) == -1) {
                        continue;
                    }
                    Set<OneTrainTripDTO> oneTrainTripDTOSCopyForCicle = new HashSet<>(oneTrainTripDTOSCopy);
                    oneTrainTripDTO = new OneTrainTripDTO();
                    localTimeOfBegining = setFirstValuesToOneTrainTripDTO(oneTrainTripDTO,
                                                                        vertex.getStation(),
                                                                        schedule,
                                                                        stopList);
                    localTimeOfEnd = setLastValuesToOneTrainTripDTO(oneTrainTripDTO,
                                                                        vertexList[endTree].getStation(),
                                                                        schedule,
                                                                        stopList);
                    //if(vertexList[parents.get(0)].equals(vertexMain)) {
                        Integer firstDistance = shortestPaths.get(parents.get(parents.size() - 1)).getDistance();
                        Integer secondDistance = shortestPaths.get(endTree).getDistance();
                        oneTrainTripDTO.setDistance(secondDistance - firstDistance);
                        //oneTrainTripDTO.setDistance(shortestPaths.get(endTree).getDistance());
//                    } else {
//                        Integer firstDistance = 0;//shortestPaths.get(parents.get(endTree)).getDistance();
//                        Integer secondDistance = shortestPaths.get(endTree).getDistance();
//                        oneTrainTripDTO.setDistance(secondDistance - firstDistance);
//                    }
                    if(localTimeOfBegining != null && localTimeOfEnd != null && localTimeOfEnd.isAfter(localTimeOfBegining)) {
                        oneTrainTripDTOSCopyForCicle.add(oneTrainTripDTO);
                        manyTrainsTripDTO.setValid(true);
                    } else {
                        manyTrainsTripDTO.setValid(false);
                    }
                    oneTrainTripDTO = new OneTrainTripDTO();
                    localTimeOfBegining = null;
                    localTimeOfEnd = null;

                    if(!manyTrainsTripDTO.isValid()){
                        continue;
                    }

                    manyTrainsTripDTO.setOneTrainTripDTOS(oneTrainTripDTOSCopyForCicle);
                    if(manyTrainsTripDTO.isValid()) {
                        manyTrainsTripDTOSet.add(manyTrainsTripDTO);
                    }
                    manyTrainsTripDTO = new ManyTrainsTripDTO();
                    oneTrainTripDTOSCopyForCicle = new HashSet<>();
                }
            } else {
                localTimeOfEnd = setLastValuesToOneTrainTripDTO(oneTrainTripDTO,
                        vertexList[endTree].getStation(),
                        prevSchedule,
                        stopList);
                if(vertexList[parents.get(0)].equals(vertexMain)) {
                    oneTrainTripDTO.setDistance(shortestPaths.get(endTree).getDistance());
                } else {
                    Integer firstDistance = 0;//shortestPaths.get(endTree).getDistance();
                    for(Integer nu = 0; nu < vertexList.length; nu++){
                        Vertex vertex = vertexList[nu];
                        if (vertex == null){
                            break;
                        }
                        if(vertex.getStation().getName().equals(oneTrainTripDTO.getStationFirst())){
                            firstDistance = shortestPaths.get(nu + 1).getDistance();
                            if(firstDistance == MAX_VERTS){
                                continue;
                            }
                            break;
                        }
                    }
                    Integer secondDistance = shortestPaths.get(endTree).getDistance();
                    oneTrainTripDTO.setDistance(secondDistance - firstDistance);
                }
                if(localTimeOfBegining != null && localTimeOfEnd != null && localTimeOfEnd.isAfter(localTimeOfBegining)) {
                    oneTrainTripDTOSCopy.add(oneTrainTripDTO);
                    manyTrainsTripDTO.setValid(true);
                } else {
                    manyTrainsTripDTO.setValid(false);
                }
                oneTrainTripDTO = new OneTrainTripDTO();
                localTimeOfBegining = null;
                localTimeOfEnd = null;

                manyTrainsTripDTO.setOneTrainTripDTOS(oneTrainTripDTOSCopy);
                oneTrainTripDTOSCopy = new HashSet<>();
            }
            if(manyTrainsTripDTO.isValid()) {
                manyTrainsTripDTOSet.add(manyTrainsTripDTO);
            }
            manyTrainsTripDTO = new ManyTrainsTripDTO();
        }
        return prevSchedule;
    }

    private LocalTime setFirstValuesToOneTrainTripDTO(OneTrainTripDTO oneTrainTripDTO,
                                                      Station station,
                                                      Schedule schedule,
                                                      List<Stop> stopList){
        oneTrainTripDTO.setStationFirst(station.getName());
        Stop stop = findStopByStationAndSchedule(station, schedule, stopList);
        if (!stop.equals(null)) {
            oneTrainTripDTO.setTimeDeparture(stop.getTimeEnd().toString());
        }
        oneTrainTripDTO.setSchedule(schedule.getTime());
        oneTrainTripDTO.setTrain(schedule.getTrain().getNumber());
        return stop.getTimeEnd();
    }

    private LocalTime setLastValuesToOneTrainTripDTO(OneTrainTripDTO oneTrainTripDTO,
                                                 Station station,
                                                 Schedule schedule,
                                                 List<Stop> stopList){
        oneTrainTripDTO.setStationLast(station.getName());
        Stop stop = findStopByStationAndSchedule(station, schedule, stopList);
        if (!stop.equals(null)) {
            oneTrainTripDTO.setTimeArrival(stop.getTimeBegining().toString());
        }
        return stop.getTimeBegining();
    }

    private Stop findStopByStationAndSchedule(Station station, Schedule schedule, List<Stop> stopList){
        for(Stop stop: stopList){
            if(stop.getStation().equals(station) && stop.getSchedule().equals(schedule)){
                return stop;
            }
        }
        return null;
    }

    public void clean() {
        countOfVertexInTree = 0;
        for (int i = 0; i < countOfVertices; i++) {
            vertexList[i].setInTree(false);
        }
    }

    private int getMin() {
        int minDist = INFINITY;
        int indexMin = 0;
        for (int i = 1; i < countOfVertices; i++) {
            if (!vertexList[i].isInTree() && shortestPaths.get(i).getDistance() < minDist) {
                minDist = shortestPaths.get(i).getDistance();
                indexMin = i;
            }
        }
        return indexMin;
    }

    private void updateShortestPaths() {
        int vertexIndex = 1;
        while (vertexIndex < countOfVertices) {

            if (vertexList[vertexIndex].isInTree()) {
                vertexIndex++;
                continue;
            }
            int currentToFringe = relationMatrix[currentVertex][vertexIndex];
            int startToFringe = startToCurrent + currentToFringe;
            int shortPathDistance = shortestPaths.get(vertexIndex).getDistance();

            if (startToFringe < shortPathDistance) {
                List<Integer> newParents = new ArrayList<>(shortestPaths.get(currentVertex).getParentVertices());
                newParents.add(currentVertex);
                shortestPaths.get(vertexIndex).setParentVertices(newParents);
                shortestPaths.get(vertexIndex).setDistance(startToFringe);
            }
            vertexIndex++;
        }
    }
    private void displayPaths() {
        for (int i = 0; i < countOfVertices; i++) {
            System.out.print(vertexList[i].getStation().getName() + " = ");
            if (shortestPaths.get(i).getDistance() == INFINITY) {
                System.out.println("0");
            } else {
                String result = shortestPaths.get(i).getDistance() + " (";
                List<Integer> parents = shortestPaths.get(i).getParentVertices();
                for (int j = 0; j < parents.size(); j++) {
                    result += vertexList[parents.get(j)].getStation().getName() + " -> ";
                }
                System.out.println(result + vertexList[i].getStation().getName() + ")");
            }
        }
    }


}
