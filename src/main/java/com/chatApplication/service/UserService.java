package com.chatApplication.service;


import com.chatApplication.dto.UserCreateResponse;
import com.chatApplication.dto.UserDto;
import com.chatApplication.exception.UserAlreadyExist;
import com.chatApplication.model.User;
import com.chatApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


@Service
public class UserService {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository ;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<User> getAllUser(){
        List<User> users =  userRepository.findAll();
        return  users;
    }


    public User findUserByUsername(String username){
        return userRepository.findByUsername(username.toLowerCase());
    }

    public UserCreateResponse registerUser(User user) throws UserAlreadyExist {
        User userExist = userRepository.findByUsername(user.getUsername().toLowerCase());

        UserCreateResponse response = new UserCreateResponse();
        if( userExist != null) {
           throw new UserAlreadyExist("user with this username already exist");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());


        user.setPassword(encodedPassword);
        userRepository.save(user);
        response.setMessage("User Created Successfully");
        response.setStatus("Success");
        response.setHttpStatus(HttpStatus.CREATED);
        return response;
    }
    public boolean deleteUser(String username) {
        User userExist = userRepository.findByUsername(username.toLowerCase());
        if(userExist == null){
            return false;
        }
        userRepository.deleteByUsername(username);
        return true;
    }
}
