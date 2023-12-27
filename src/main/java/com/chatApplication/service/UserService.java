package com.chatApplication.service;


import com.chatApplication.config.JwtService;
import com.chatApplication.dto.AuthenticationRequest;
import com.chatApplication.dto.RegisterRequest;
import com.chatApplication.dto.UserAuthenticationResponse;
import com.chatApplication.exception.UserAlreadyExist;
import com.chatApplication.model.User;
import com.chatApplication.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository ;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;


    public UserAuthenticationResponse registerUser(RegisterRequest registerRequest, HttpServletResponse httpServletResponse) throws UserAlreadyExist {

        Optional<User> userExist = Optional.ofNullable(userRepository.findByUsername(registerRequest.getUsername().toLowerCase()));

        UserAuthenticationResponse response = new UserAuthenticationResponse();
        if( userExist.isPresent()) {
            throw new UserAlreadyExist("user with this username already exist");
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPasscode());

        var user = User.builder()
                .username(registerRequest.getUsername())
                .password(encodedPassword)
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        Cookie cookie = new Cookie("jwt-Token",token);
        cookie.setMaxAge(600);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return UserAuthenticationResponse.builder()
                .token(token)
                .message("User Registered Successfully")
                .status("Success")
                .build();
    }

    public UserAuthenticationResponse authenticate(AuthenticationRequest loginForm, HttpServletResponse httpServletResponse) throws Exception {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(loginForm.getUsername()));

        if(optionalUser.isPresent() == false){
            throw new Exception("User does not exist");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getUsername(),
                        loginForm.getPasscode()
                )
        );

        User user = optionalUser.get();
        String token = jwtService.generateToken(user);
        Cookie cookie = new Cookie("jwt-Token",token);
        cookie.setMaxAge(600);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return UserAuthenticationResponse.builder()
                .token(token)
                .status("success")
                .httpStatus(HttpStatus.OK)
                .message("logged In successfully")
                .build();
    }


    public List<User> getAllUser(){
        List<User> users =  userRepository.findAll();
        return  users;
    }


    public User findUserByUsername(String username){
        return userRepository.findByUsername(username.toLowerCase());
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
