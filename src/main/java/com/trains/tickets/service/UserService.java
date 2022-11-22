package com.trains.tickets.service;

import com.trains.tickets.domain.User;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public UserService() {
    }

    public boolean addUser(User user) {
        User userFromBD = userRepository.findByLogin(user.getLogin());
        if(userFromBD != null){
            return false;
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassenger(passengerRepository.findByPassport("2222222222"));
        user.setRole(roleRepository.findByName("user"));
        userRepository.save(user);
        return true;
    }
}
