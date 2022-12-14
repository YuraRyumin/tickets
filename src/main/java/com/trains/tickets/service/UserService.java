package com.trains.tickets.service;

import com.trains.tickets.domain.Passenger;
import com.trains.tickets.domain.Role;
import com.trains.tickets.domain.User;
import com.trains.tickets.dto.UserDTO;
import com.trains.tickets.dto.UserForNavDTO;
import com.trains.tickets.repository.PassengerRepository;
import com.trains.tickets.repository.RoleRepository;
import com.trains.tickets.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Slf4j
@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PassengerRepository passengerRepository;
    private final MailSender mailSender;
    private final RoleService roleService;
    private final PassengerService passengerService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, PassengerRepository passengerRepository, MailSender mailSender, RoleService roleService, PassengerService passengerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.passengerRepository = passengerRepository;
        this.mailSender = mailSender;
        this.roleService = roleService;
        this.passengerService = passengerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }


    @Transactional
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

    @Transactional
    public void activateUser(String code, Model model) {
        User user = userRepository.findByActivationCode(code);
        if(user == null){
            model.addAttribute("message", "Activation cod not found");
        }
        user.setActivationCode(null);
        userRepository.save(user);
        model.addAttribute("message", "User successfully activated");
    }

    public String putInfoAboutUserToModel(String userThis, Model model){
        if (userThis.equals("new")) {
            model.addAttribute("userThis", getEmptyDto());
            model.addAttribute("roles", roleService.convertAllEntityToDto(roleRepository.findAll()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDto(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))));
        } else {
            User selectedUser = userRepository.findByUuid(userThis);
            if(selectedUser == null){
                throw  new NullPointerException("User not found!");
            }
            model.addAttribute("userThis", convertEntityToDto(selectedUser));
            model.addAttribute("roles", roleService.convertAllEntityToDtoWithSelected(roleRepository.findAll(), selectedUser.getRole()));
            model.addAttribute("passengers", passengerService.convertAllEntityToDtoWithSelected(passengerRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), selectedUser.getPassenger()));
        }
        return "userEdit";
    }

    public String putInfoAboutAllUsersToModel(Model model){
        model.addAttribute("users", convertAllEntityToDto(userRepository.findAll()));
        return "userList";
    }

    @Transactional
    public String saveUser(String email, String telephone, String login, String password, String activationCode, String passenger, String role, Integer userId, User user){
        Passenger passengerNew = null;
        if(passenger != null && !passenger.equals("")){
            String[] fullName = passenger.split("\\s");
            if(fullName.length > 0) {
                String nameOfPassenger = fullName[0];
                if(fullName.length > 1) {
                    String surnameOfPassenger = fullName[1];
                    passengerNew = passengerRepository.findByNameAndSurname(nameOfPassenger, surnameOfPassenger);
                } else {
                    passengerNew = passengerRepository.findByName(nameOfPassenger);
                }
            }
        }

        Role roleNew = roleRepository.findByName(role);
        if (userId.equals(0)) {
            User userChanged = new User(
                    email,
                    telephone,
                    login,
                    passwordEncoder.encode(password),
                    passengerNew,
                    roleNew,
                    true,
                    activationCode,
                    UUID.randomUUID().toString()
            );
            if(userChanged.getEmail() != ""){
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcom to Trains. Please visit next link: http://localhost:8080/activate/%s",
                        userChanged.getLogin(), userChanged.getActivationCode()
                );
                mailSender.send(userChanged.getEmail(), "Activation", message);
            }
            userRepository.save(userChanged);
            if(user != null && userChanged != null) {
                log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " create new user with id " +
                        userChanged.getId() + " (" +
                        userChanged.getLogin() + "; " +
                        userChanged.getEmail() + "; " +
                        userChanged.getTelephone() + "; " +
                        //userChanged.getRole() == null ? null : userChanged.getRole().getName() + "; " +
                        //userChanged.getPassenger() == null ? null : userChanged.getPassenger().getName() + " " +
                        //userChanged.getPassenger() == null ? null : userChanged.getPassenger().getSurname() + "; " +
                        userChanged.getUuid() + "; " +
                        userChanged.getActivationCode() + ")");
            }
        } else {
            User userChanged = userRepository.findById(userId);
            boolean wasChanged = false;
            if(!userChanged.getEmail().equals(email)){
                userChanged.setEmail(email);
                wasChanged = true;
            }
            if(!userChanged.getTelephone().equals(telephone)){
                userChanged.setTelephone(telephone);
                wasChanged = true;
            }
            if(!userChanged.getLogin().equals(login)){
                userChanged.setLogin(login);
                wasChanged = true;
            }

            if (userChanged.getUuid().equals("") || userChanged.getUuid() == null){
                userChanged.setUuid(UUID.randomUUID().toString());
                wasChanged = true;
            }

            if(!userChanged.getRole().equals(roleNew)){
                userChanged.setRole(roleNew);
                wasChanged = true;
            }
            userChanged.setActive(true);

            if(wasChanged){
                userRepository.save(userChanged);
                if(user != null && userChanged != null) {
                    log.info(LocalDateTime.now().toString() + " - " + user.getLogin() + " change user with id " +
                            userChanged.getId() + " (" +
                            userChanged.getLogin() + "; " +
                            userChanged.getEmail() + "; " +
                            userChanged.getTelephone() + "; " +
                            //userChanged.getRole() == null ? "" : userChanged.getRole().getName() + "; " +
                            //userChanged.getPassenger() == null ? "" : userChanged.getPassenger().getName() + " " +
                            //userChanged.getPassenger() == null ? "" : userChanged.getPassenger().getSurname() + "; " +
                            userChanged.getUuid() + "; " +
                            userChanged.getActivationCode() + ")");
                }
            }
        }
        return "redirect:/user";
    }

    public boolean isAdmin(User user){
        return user.getRole().getName().equals("admin");
    }

    public boolean isOperator(User user){
        return user.getRole().getName().equals("operator");
    }
}
