package com.trains.tickets.service;

import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RoleServiceTest {
    @Mock
    RoleRepository roleRepositoryMockito;

    @InjectMocks
    RoleService roleService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRole() {
        when(roleRepositoryMockito.save(any(Role.class))).thenReturn(new Role());
        Assertions.assertDoesNotThrow(() -> roleService.saveRole("Abc", 0, new User()));
    }

    @Test
    void anotherSaveRole(){
        Role role = new Role("Abs");
        role.setId(4);
        when(roleRepositoryMockito.findById(4)).thenReturn(role);
        when(roleRepositoryMockito.save(role)).thenReturn(role);
        Assertions.assertDoesNotThrow(() -> roleService.saveRole(role.getName() + "1", role.getId(), new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(roleService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(roleService.getEmptyDto().getName(), "");
    }
}