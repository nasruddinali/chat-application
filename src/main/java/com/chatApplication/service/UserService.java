package com.chatApplication.service;


import com.chatApplication.dto.*;
import com.chatApplication.exception.InvalidPassword;
import com.chatApplication.exception.UserAlreadyExist;
import com.chatApplication.model.User;
import com.chatApplication.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository ;


    public List<User> getAllUser(){
        List<User> users =  userRepository.findAll();
        return  users;
    }

    public UserCreateResponse registerUser(UserCreateRequest request) throws UserAlreadyExist {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(request.getUsername().toLowerCase()));

        UserCreateResponse response = new UserCreateResponse();
        if( optionalUser.isPresent()) {
           throw new UserAlreadyExist("user with this username already exist");
        }
        String encodedPassword = encodePassword(request.getPasscode());

        User user = User.builder()
                .password(encodedPassword)
                .username(request.getUsername())
                .build();
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

    public UserAuthenticationResponse authenticate(AuthenticationRequest loginForm) throws Exception {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(loginForm.getUsername()));

        if(optionalUser.isPresent() == false){
            throw new Exception("User does not exist");
        }
        User user = optionalUser.get();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        boolean decodedPassword = passwordEncoder.matches("passcode1",passwordEncoder.encode("passcode1"));
        if(!isPasswordCorrect(loginForm.getPasscode(), user.getPassword())) {
            return UserAuthenticationResponse.builder()
                    .status("failure")
                    .message("validation failed")
                    .build();
//            throw new InvalidPassword("Password is invalid");
        }

        return UserAuthenticationResponse.builder()
                .status("success")
                .httpStatus(HttpStatus.OK)
                .message("logged In successfully")
                .build();
    }
    private boolean isPasswordCorrect(String simplePassword, String encodedPassword) {
        return passwordEncoder.matches(simplePassword,encodedPassword);
    }
    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
