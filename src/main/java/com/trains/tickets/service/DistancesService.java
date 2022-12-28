package com.trains.tickets.service;

import com.trains.tickets.domain.Distance;
import com.trains.tickets.domain.Station;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.DistanceDTO;
import com.trains.tickets.repository.DistanceRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Slf4j
@Transactional(readOnly = true)
@Service
public class DistancesService {
    private final UserService userService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final DistanceRepository distanceRepository;
    private final StationService stationService;
    private final StationRepository stationRepository;


    public DistancesService(UserService userService, RoleService roleService, RoleRepository roleRepository, DistanceRepository distanceRepository, StationService stationService, StationRepository stationRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.distanceRepository = distanceRepository;
        this.stationService = stationService;
        this.stationRepository = stationRepository;
    }

    public Iterable<DistanceDTO> convertAllEntityToDto(Iterable<Distance> distances){
        LinkedHashSet<DistanceDTO> collect = StreamSupport.stream(distances.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return collect;
    }

    public DistanceDTO convertEntityToDto(Distance distance){
        DistanceDTO distanceDTO = new DistanceDTO();

        distanceDTO.setId(distance.getId());
        distanceDTO.setStationFirst(distance.getStationFirst().getName());
        distanceDTO.setStationLast(distance.getStationLast().getName());
        distanceDTO.setKilometers(distance.getKilometers());

        return distanceDTO;
    }

    public DistanceDTO getEmptyDto(){
        DistanceDTO distanceDTO = new DistanceDTO();

        distanceDTO.setId(0);
        distanceDTO.setStationFirst("");
        distanceDTO.setStationLast("");
        distanceDTO.setKilometers(0);

        return distanceDTO;
    }

    public void putInfoAboutDistanceToModel(User user, Model model, String distance){
        if (distance.equals("new")) {
            model.addAttribute("distance", getEmptyDto());
            model.addAttribute("stationsFirst", stationService.convertAllEntitysToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
            model.addAttribute("stationsLast", stationService.convertAllEntitysToDto(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        } else {
            Distance selectedDistance = distanceRepository.findById(Integer.parseInt(distance));
            model.addAttribute("distance", convertEntityToDto(selectedDistance));
            model.addAttribute("stationsFirst", stationService.convertAllEntityToDtoWithSelected(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedDistance.getStationFirst()));
            model.addAttribute("stationsLast", stationService.convertAllEntityToDtoWithSelected(stationRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedDistance.getStationLast()));
        }
    }
    @Transactional
    public void saveDistance(Integer distanceId, Integer kilometers, String stationFirst, String stationLast, User user){
        if (distanceId.equals(0)) {
            Station stationFirstNew = stationRepository.findByName(stationFirst);
            Station stationLastNew = stationRepository.findByName(stationLast);
            if(stationFirstNew != null && stationLastNew != null) {
                Distance distanceChanged = new Distance(
                        stationFirstNew,
                        stationLastNew,
                        kilometers
                );
                distanceRepository.save(distanceChanged);
                log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new distance with id " +
                        distanceChanged.getId() + " (" +
                        distanceChanged.getStationFirst().getName() + "; " +
                        distanceChanged.getStationLast().getName() + "; " +
                        distanceChanged.getKilometers() + ")");
            }
        } else {
            Distance distanceChanged = distanceRepository.findById(distanceId);
            boolean wasChanged = false;
            Station stationFirstNew = stationRepository.findByName(stationFirst);
            if(stationFirstNew != null) {
                if (!distanceChanged.getStationFirst().getName().equals(stationFirstNew.getName())) {
                    distanceChanged.setStationFirst(stationFirstNew);
                    wasChanged = true;
                }
            }
            Station stationLastNew = stationRepository.findByName(stationLast);
            if(stationLastNew != null) {
                if (!distanceChanged.getStationLast().getName().equals(stationLastNew.getName())) {
                    distanceChanged.setStationLast(stationLastNew);
                    wasChanged = true;
                }
            }
            if(!distanceChanged.getKilometers().equals(kilometers)){
                distanceChanged.setKilometers(kilometers);
                wasChanged = true;
            }
            if(wasChanged){
                distanceRepository.save(distanceChanged);
                log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " change distance with id " +
                        distanceChanged.getId() + " (" +
                        distanceChanged.getStationFirst().getName() + "; " +
                        distanceChanged.getStationLast().getName() + "; " +
                        distanceChanged.getKilometers() + ")");
            }
        }
    }
}
