package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Role;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.dto.RoleDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoleService {
    public Iterable<RoleDTO> convertAllEntityToDto(Iterable<Role> roles){
        return StreamSupport.stream(roles.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<RoleDTO> convertAllEntityToDtoWithSelected(Iterable<Role> roles, Role selectedRole){
        return StreamSupport.stream(roles.spliterator(), false)
                .map(role -> {
                    RoleDTO roleDTO = convertEntityToDto(role);
                    if (role.getName().equals(selectedRole.getName())){
                        roleDTO.setSelected(true);
                    } else {
                        roleDTO.setSelected(false);
                    }
                    return roleDTO;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public RoleDTO convertEntityToDto(Role role){
        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());

        return roleDTO;
    }

    public RoleDTO getEmptyDto(){
        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId(0);
        roleDTO.setName("");

        return roleDTO;
    }
}
