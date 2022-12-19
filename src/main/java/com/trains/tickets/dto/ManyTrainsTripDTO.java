package com.trains.tickets.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ManyTrainsTripDTO {
    private Set<OneTrainTripDTO> oneTrainTripDTOS;
    private boolean valid;
}
