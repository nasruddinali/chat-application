package com.chatApplication.controller;

import com.chatApplication.config.JwtService;
import com.chatApplication.dto.MessageFromSingleUserResponse;
import com.chatApplication.dto.MessageDto;
import com.chatApplication.dto.SingleUser;
import com.chatApplication.exception.ActionNotAllowed;
import com.chatApplication.model.Message;
import com.chatApplication.model.User;
import com.chatApplication.service.MessageService;
import com.chatApplication.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final MessageService messageService;
    @Autowired
    private final JwtService jwtService;

    @GetMapping("/example")
    public String exampleEndpoint(@RequestParam String paramName) {
        // Your code here
        return "Value of paramName: " + paramName;
    }

    @GetMapping("/user/{username}/message")
    public ResponseEntity<MessageFromSingleUserResponse> getChatHistory(@RequestParam(value = "friendUsername",required = false) String friendUsername,
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
    public ResponseEntity<String> sendMessage( @RequestBody MessageDto  msg ,@CookieValue(name = "jwt-Token") String token) {
        try {
//            MessageDto msg = new MessageDto();
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
