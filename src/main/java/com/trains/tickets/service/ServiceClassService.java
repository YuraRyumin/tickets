package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.ServiceClass;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.dto.ServiceClassDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ServiceClassService {
    public Iterable<ServiceClassDTO> convertAllEntityToDto(Iterable<ServiceClass> serviceClasses){
        return StreamSupport.stream(serviceClasses.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<ServiceClassDTO> convertAllEntityToDtoWithSelected(Iterable<ServiceClass> serviceClasses, ServiceClass selectedServiceClass){
        return StreamSupport.stream(serviceClasses.spliterator(), false)
                .map(serviceClass -> {
                    ServiceClassDTO serviceClassDTO = convertEntityToDto(serviceClass);
                    if (serviceClass.getName().equals(selectedServiceClass.getName())){
                        serviceClassDTO.setSelected(true);
                    } else {
                        serviceClassDTO.setSelected(false);
                    }
                    return serviceClassDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ServiceClassDTO convertEntityToDto(ServiceClass serviceClass){
        ServiceClassDTO serviceClassDTO = new ServiceClassDTO();

        serviceClassDTO.setId(serviceClass.getId());
        serviceClassDTO.setName(serviceClass.getName());
        serviceClassDTO.setPrisePerKm(serviceClass.getPrisePerKm());

        return serviceClassDTO;
    }

    public ServiceClassDTO getEmptyDto(){
        ServiceClassDTO serviceClassDTO = new ServiceClassDTO();

        serviceClassDTO.setId(0);
        serviceClassDTO.setName("");
        serviceClassDTO.setPrisePerKm((float) 0);

        return serviceClassDTO;
    }
}
