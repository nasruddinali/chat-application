package com.chatApplication.controller;


import com.chatApplication.dto.*;
import com.chatApplication.model.User;
import com.chatApplication.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Transactional
public class UserController {



    @Autowired
    private final UserService userService;


    @PostMapping("/user")
    public ResponseEntity<UserCreateResponse> registerUser(@RequestBody UserCreateRequest userDto,HttpServletResponse httpServletResponse) {
        try {
            userService.registerUser(userDto,httpServletResponse);
            UserCreateResponse response = UserCreateResponse.builder()
                    .status("success")
                    .message("User registered Successfully")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            UserCreateResponse response = UserCreateResponse.builder()
                    .status("failure")
                    .message("User already exist")
                    .build();


            return new ResponseEntity<>(response,HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/user")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AllUserResponse getAllUser(){
        List<User> users =  userService.getAllUser();

        List<String> usernames = users.stream().map(user -> user.getUsername()).toList();

        AllUserResponse response = new AllUserResponse();
        response.setUsers(usernames);
        response.setStatus("success");
        return response;
    }
    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest loginForm, HttpServletResponse httpServletResponse) throws Exception {
        var response =  userService.authenticate(loginForm,httpServletResponse);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User getUser(@PathVariable String username){
        return userService.findUserByUsername(username);
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        User userToDelete = userService.findUserByUsername(username);

        if (userToDelete != null) {
            userService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    @GetMapping("/logout")
    public void logout(HttpServletResponse httpServletResponse){
    }
}
