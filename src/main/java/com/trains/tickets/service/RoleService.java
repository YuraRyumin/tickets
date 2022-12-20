package com.trains.tickets.service;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.RoleDTO;
import com.trains.tickets.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
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
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

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
    @Transactional
    public void saveRole(String name, Integer roleId, User user){
        if (roleId.equals(0)) {
            Role roleChanged = new Role(name);
            roleRepository.save(roleChanged);
            log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new role with id " +
                    roleChanged.getId() + " (" +
                    roleChanged.getName() + ")");
        } else {
            Role roleChanged = roleRepository.findById(roleId);
            if(!roleChanged.getName().equals(name)){
                roleChanged.setName(name);
                roleRepository.save(roleChanged);
                log.error(LocalDateTime.now().toString() + " - " + user.getLogin() + " change role with id " +
                        roleChanged.getId() + " (" +
                        roleChanged.getName() + ")");
            }
        }
    }

    public void putInfoAboutRoleToModel(Model model, String role){
        if (role.equals("new")) {
            model.addAttribute("role", getEmptyDto());
        } else {
            model.addAttribute("role", convertEntityToDto(roleRepository.findById(Integer.parseInt(role))));
        }
    }
}
