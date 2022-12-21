package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    UserRepository userRepositoryMockito;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser() {
        when(userRepositoryMockito.save(any(User.class))).thenReturn(new User());
        Assertions.assertDoesNotThrow(() -> userService.saveUser("email",
                                                                "telephone",
                                                                "login",
                                                                "password",
                                                                "activationCode",
                                                                "passenger",
                                                                "role",
                                                                0,
                                                                new User()));
    }

    @Test
    void anotherSaveUser(){
        User user = new User("email",
                            "telephone",
                            "login",
                            "password",
                            new Passenger(),
                            new Role(),
                            true,
                            "activationCode",
                            "uuid");
        user.setId(4);
        when(userRepositoryMockito.findById(4)).thenReturn(user);
        when(userRepositoryMockito.save(user)).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userService.saveUser("email",
                                                                "telephone",
                                                                "login",
                                                                "password",
                                                                "activationCode",
                                                                "passenger",
                                                                "role",
                                                                5,
                                                                new User()));
    }

    @Test
    void testEmptyDto(){
        Assertions.assertEquals(userService.getEmptyDto().getId(), 0);
        Assertions.assertEquals(userService.getEmptyDto().getLogin(), "");
        Assertions.assertEquals(userService.getEmptyDto().getEmail(), "");
        Assertions.assertEquals(userService.getEmptyDto().getTelephone(), "");
        Assertions.assertEquals(userService.getEmptyDto().getPassenger(), "");
        Assertions.assertEquals(userService.getEmptyDto().getPassword(), "");
        Assertions.assertEquals(userService.getEmptyDto().getActivationCode(), "");
        Assertions.assertEquals(userService.getEmptyDto().getRole(), "");
    }

    @Test
    void testActivateUser(){
        //when(userService.activateUser("123")).thenReturn(new User());
    }
}