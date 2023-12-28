package com.chatApplication.controller;


import com.chatApplication.config.JwtService;
import com.chatApplication.dto.*;
import com.chatApplication.exception.ActionNotAllowed;
import com.chatApplication.model.Message;
import com.chatApplication.model.User;
import com.chatApplication.service.MessageService;
import com.chatApplication.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping
@RequiredArgsConstructor
@Transactional
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final MessageService messageService;
    @Autowired
    private final JwtService jwtService;


    @PostMapping("/user")
    public ResponseEntity<UserAuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletResponse httpServletResponse) {
        try {
            User user = new User().builder()
                    .password(registerRequest.getPasscode())
                    .username(registerRequest.getUsername()).build();
            UserAuthenticationResponse response = userService.registerUser(registerRequest,httpServletResponse );
            response.setStatus("success");
            response.setMessage("User registered Successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            UserAuthenticationResponse response = new UserAuthenticationResponse();
            response.setStatus("failure");
            response.setMessage("User already exist");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/user/login")
    public ResponseEntity<UserAuthenticationResponse> loginUser(@RequestBody AuthenticationRequest loginForm,
                                                                HttpServletResponse httpServletResponse) throws Exception {
         UserAuthenticationResponse response =  userService.authenticate(loginForm,httpServletResponse);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/user")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AllUserResponse getAllUser() {
        List<User> users = userService.getAllUser();

        List<String> usernames = users.stream().map(user -> user.getUsername()).toList();

        AllUserResponse response = new AllUserResponse();
        response.setUsers(usernames);
        response.setStatus("success");
        return response;
    }


    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User getUser(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }


//    return new ResponseEntity<>(message, HttpStatus.CREATED);

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
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

    //////////


    @GetMapping("/user/{username}/message")
    public ResponseEntity<MessageFromSingleUserResponse> getChatHistory(@RequestParam(name = "friendUsername",required = false) String friendUsername,
                                                                        @PathVariable String username,
                                                                        @CookieValue(name = "jwt-Token") String token) throws ActionNotAllowed {

        Optional<User> optionalReceiver = Optional.ofNullable(userService.findUserByUsername(username));

        if (!optionalReceiver.isPresent()) {
            throw new UsernameNotFoundException("Receiver not found");
        }

        User user = optionalReceiver.get();
        if (!user.getUsername().equals(jwtService.extractUsername(token))) {
            throw new ActionNotAllowed("Action not allowed");
        }
        if (!jwtService.isTokenValid(token, user)) {
            throw new ActionNotAllowed("Action not allowed");
        }
        MessageFromSingleUserResponse chatHistory = new MessageFromSingleUserResponse();
        if (friendUsername != null ) {
            User friend = userService.findUserByUsername(friendUsername);
            chatHistory = messageService.getChatHistoryOfAUserWithFriend(friend, user);
        } else chatHistory = messageService.getChatHistoryOfAUser(user);

        return new ResponseEntity<>(chatHistory, HttpStatus.OK);

    }
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage( @CookieValue(name = "jwt-Token") String token) {
        try {
            MessageDto msg = new MessageDto();
            String sender = msg.getSender();
            String receiver = msg.getReceiver();
            String content = msg.getText();
            Message message = messageService.sendMessage(sender, receiver, content, token);
            return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Failure", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all-msg")
    public ResponseEntity<List<Message>> getAllMessage() {
        List<Message> messages = messageService.getAllMsg();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
