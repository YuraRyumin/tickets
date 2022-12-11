package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.PassengerDTO;
import com.trains.tickets.dto.RoleDTO;
import com.trains.tickets.dto.UserDTO;
import com.trains.tickets.dto.UserForNavDTO;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.StationRepository;
import com.trains.tickets.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PassengerRepository passengerRepository;
    private final MailSender mailSender;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, PassengerRepository passengerRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.passengerRepository = passengerRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }



    public String addUser(User user, Map<String, Object> model) {
        User userFromDB = userRepository.findByLogin(user.getLogin());
        if(userFromDB != null){
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassenger(passengerRepository.findByPassport("2222222222"));
        user.setRole(roleRepository.findByName("user"));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setUuid(UUID.randomUUID().toString());
        userRepository.save(user);
        if(user.getEmail() != ""){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcom to Trains. Please visit next link: http://localhost:8080/activate/%s",
                    user.getLogin(), user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation", message);
        }
        return "redirect:/login";
    }

    public Iterable<UserDTO> convertAllEntityToDtoWithSelected(Iterable<User> users, User selectedUser){
        return StreamSupport.stream(users.spliterator(), false)
                .map(user -> {
                    UserDTO userDTO = convertEntityToDto(user);
                    if (user.getLogin().equals(selectedUser.getLogin())){
                        userDTO.setSelected(true);
                    } else {
                        userDTO.setSelected(false);
                    }
                    return userDTO;
                    })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Iterable<UserDTO> convertAllEntityToDto(Iterable<User> users){
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::convertEntityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public UserForNavDTO convertEntityToDtoForNav(User user) {
        if(user == null){
            return null;
        } else {
            UserForNavDTO userForNavDTO = new UserForNavDTO();

            userForNavDTO.setLogin(user.getLogin());
            userForNavDTO.setUuid(user.getUuid());

            return userForNavDTO;
        }
    }

    public UserDTO convertEntityToDto(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setEmail(user.getEmail());
        userDTO.setTelephone(user.getTelephone());
        if(user.getPassenger() == null){
            userDTO.setPassenger("");
        } else {
            userDTO.setPassenger(user.getPassenger().getName() + " " + user.getPassenger().getSurname());
        }
        userDTO.setPassword(user.getPassword());
        userDTO.setActive(user.isActive());
        userDTO.setActivationCode(user.getActivationCode());
        userDTO.setRole(user.getRole().getName());
        userDTO.setUuid(user.getUuid());

        return userDTO;
    }

    public UserDTO getEmptyDto(){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(0);
        userDTO.setLogin("");
        userDTO.setEmail("");
        userDTO.setTelephone("");
        userDTO.setPassenger("");
        userDTO.setPassword("");
        userDTO.setActive(true);
        userDTO.setActivationCode("");
        userDTO.setRole("");

        return userDTO;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if(user == null){
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }
}
