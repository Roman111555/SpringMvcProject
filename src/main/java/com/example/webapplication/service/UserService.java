package com.example.webapplication.service;

import com.example.webapplication.domain.Role;
import com.example.webapplication.domain.User;
import com.example.webapplication.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SMTPMailSender smtpMailSender;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    private String hostname;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if(user == null){
            throw new  UsernameNotFoundException("User not found!");
        }
        return user;
    }

    public boolean addUser(User user){
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if(userFromDB != null){
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        sentMessage(user);
        return true;
    }

    private void sentMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())){
        String message = String.format("Hello, %s! \n +" +
                "Welcome to WebApplication. Please, visit next link: http://%s/activate/%s",
                 user.getUsername(), hostname, user.getActivationCode());

            smtpMailSender.send(user.getEmail(), "ActivationCode", message);
        }
    }

    public boolean isActivateUser(String code) {

        User user = userRepo.findByActivationCode(code);
        if (user == null){
            return false;
        }
        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }

    public List<User> findAll() {
      return  userRepo.findAll();
    }

    public User findUserById(Long id) {
      return  userRepo.findById(id).get();
    }

    public void delete(Long userId) {
        userRepo.delete(findUserById(userId));
    }

    public void saveUser(Long userId, String username, Map<String, String> form) {
        User user = findUserById(userId);
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for(String key : form.keySet()){
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

       boolean isEmailChanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));

       if(isEmailChanged){
           user.setEmail(email);

           if(!StringUtils.isEmpty(email)){
               user.setActivationCode(UUID.randomUUID().toString());
           }
       }
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        userRepo.save(user);
        if(isEmailChanged){
            sentMessage(user);
        }
    }

    public void subscribe(User currentUser, Long userId) {
        User user = userRepo.findById(userId).get();
        user.getSubscribers().add(currentUser);

        userRepo.save(user);
    }

    public void unSubscribe(User currentUser, Long userId) {
        User user = userRepo.findById(userId).get();
        user.getSubscribers().remove(currentUser);

        userRepo.save(user);
    }
}
