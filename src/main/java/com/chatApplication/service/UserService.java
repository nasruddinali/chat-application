package com.chatApplication.service;


import com.chatApplication.config.JwtUtil;
import com.chatApplication.dto.*;
import com.chatApplication.exception.ErrorRes;
import com.chatApplication.exception.InvalidPassword;
import com.chatApplication.exception.UserAlreadyExist;
import com.chatApplication.model.User;
import com.chatApplication.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private final AuthenticationManager authenticationManager;


    private final JwtUtil jwtUtil;


    public List<User> getAllUser(){
        List<User> users =  userRepository.findAll();
        return  users;
    }

    public ResponseEntity<?> registerUser(UserCreateRequest request,HttpServletResponse httpServletResponse) throws UserAlreadyExist {
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(request.getUsername().toLowerCase()));
            if (optionalUser.isPresent()) {
                throw new UserAlreadyExist("user with this username already exist");
            }
            String encodedPassword = encodePassword(request.getPasscode());

            User user = User.builder()
                    .password(encodedPassword)
                    .username(request.getUsername())
                    .build();
            userRepository.save(user);
        UserCreateResponse response = UserCreateResponse.builder()
                .message("User created successfully")
                .status("success")
                .httpStatus(HttpStatus.CREATED)
                .build();

        String token = jwtUtil.generateToken(user);
        Cookie cookie = new Cookie("token",token);
        cookie.setMaxAge(600);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(response);
    }


    public boolean deleteUser(String username) {
        User userExist = userRepository.findByUsername(username.toLowerCase());
        if(userExist == null){
            return false;
        }
        userRepository.deleteByUsername(username);
        return true;
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest loginForm,HttpServletResponse httpServletResponse) throws Exception {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPasscode()));
            String email = authentication.getName();
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User.builder()
                    .username(loginForm.getUsername())
                    .password(loginForm.getPasscode())
                    .build();
            String token = jwtUtil.generateToken(user);
             UserAuthenticationResponse response= UserAuthenticationResponse.builder()
                     .message("logged in successfully")
                     .token(token)
                     .status("success")
                     .build();

            Cookie cookie = new Cookie("token",token);
            cookie.setMaxAge(600);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
//        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(loginForm.getUsername()));
//
//        if(optionalUser.isPresent() == false){
//            throw new Exception("User does not exist");
//        }
//        User user = optionalUser.get();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        boolean decodedPassword = passwordEncoder.matches("passcode1",passwordEncoder.encode("passcode1"));
//        if(!isPasswordCorrect(loginForm.getPasscode(), user.getPassword())) {
//            return UserAuthenticationResponse.builder()
//                    .status("failure")
//                    .message("validation failed")
//                    .build();
//        }

//        return UserAuthenticationResponse.builder()
//                .status("success")
//                .httpStatus(HttpStatus.OK)
//                .message("logged In successfully")
//                .build();
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
