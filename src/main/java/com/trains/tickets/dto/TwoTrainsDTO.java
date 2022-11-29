package com.trains.tickets.dto;

import lombok.Data;

@Data
public class TwoTrainsDTO {
    private String timeBeginningFirstTrain;
    private String nameFirstStationFirstTrain;
    private String nameSecondStationFirstTrain;
    private String timeEndFirstTrain;
    private String timeBeginningSecondTrain;
    private String nameFirstStationSecondTrain;
    private String nameSecondStationSecondTrain;
    private String timeEndSecondTrain;
}
