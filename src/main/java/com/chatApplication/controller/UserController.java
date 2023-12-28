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

import java.security.Principal;
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
    public ResponseEntity<UserCreateResponse> registerUser(@RequestBody UserCreateRequest userDto) {
        try {
            userService.registerUser(userDto);
            UserCreateResponse response = new UserCreateResponse();
            response.setStatus("success");
            response.setMessage("User registered Successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            UserCreateResponse response = new UserCreateResponse();
            response.setStatus("failure");
            response.setMessage("User already exist");
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
    public ResponseEntity<UserAuthenticationResponse> loginUser(@RequestBody AuthenticationRequest loginForm) throws Exception {
        UserAuthenticationResponse response =  userService.authenticate(loginForm);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User getUser(@PathVariable String username){
        return userService.findUserByUsername(username);
    }




//    return new ResponseEntity<>(message, HttpStatus.CREATED);

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

//    @GetMapping("/current-user")
//    public String getLoggedInUser(Principal principal) {
//        return principal.getName();
//    }
}
