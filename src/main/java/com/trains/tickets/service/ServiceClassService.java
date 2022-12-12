package com.trains.tickets.service;

import com.trains.tickets.domain.ServiceClass;
import com.trains.tickets.dto.ServiceClassDTO;
import com.trains.tickets.repository.ServiceClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ServiceClassService {
    private final ServiceClassRepository serviceClassRepository;

    public ServiceClassService(ServiceClassRepository serviceClassRepository) {
        this.serviceClassRepository = serviceClassRepository;
    }

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

    public void putInfoAboutServiceClassToModel(String serviceClass, Model model){
        if (serviceClass.equals("new")) {
            model.addAttribute("serviceClass", getEmptyDto());
        } else {
            model.addAttribute("serviceClass", convertEntityToDto(serviceClassRepository.findById(Integer.parseInt(serviceClass))));
        }
    }

    public void saveServiceClass(String name,
                                 Float prisePerKm,
                                 Integer serviceClassId){
        if (serviceClassId.equals(0)) {
            ServiceClass serviceClassChanged = new ServiceClass(
                    name,
                    prisePerKm
            );
            serviceClassRepository.save(serviceClassChanged);
        } else {
            ServiceClass serviceClassChanged = serviceClassRepository.findById(serviceClassId);
            boolean wasChanged = false;
            if(!serviceClassChanged.getName().equals(name)){
                serviceClassChanged.setName(name);
                wasChanged = true;
            }
            if(!serviceClassChanged.getPrisePerKm().equals(prisePerKm)){
                serviceClassChanged.setPrisePerKm(prisePerKm);
                wasChanged = true;
            }
            if(wasChanged){
                serviceClassRepository.save(serviceClassChanged);
            }
        }
    }
}
