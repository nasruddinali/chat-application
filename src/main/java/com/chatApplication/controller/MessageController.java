package com.chatApplication.controller;

import com.chatApplication.dto.MessageFromSingleUserResponse;
import com.chatApplication.dto.SendMessageDto;
import com.chatApplication.model.Message;
import com.chatApplication.model.User;
import com.chatApplication.service.MessageService;
import com.chatApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final MessageService messageService;


    @GetMapping("/user/{username}/message")
    public ResponseEntity<MessageFromSingleUserResponse> getChatHistory(@PathVariable String username,
                                                                        @RequestParam(required = false) String friendUsername) {

        User receiver = userService.findUserByUsername(username);

        MessageFromSingleUserResponse chatHistory = new MessageFromSingleUserResponse();
        if(friendUsername != null){
            User friend = userService.findUserByUsername(friendUsername);
            chatHistory = messageService.getChatHistoryOfAUserWithFriend(friend,receiver);
        }
        else
            chatHistory = messageService.getChatHistoryOfAUser( receiver);
        return new ResponseEntity<>(chatHistory, HttpStatus.OK);

    }
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageDto msg) {
        try {
            String sender = msg.getSender();
            String receiver = msg.getReceiver();
            String content = msg.getText();
            Message message = messageService.sendMessage(sender, receiver, content);
            return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Failure", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all-msg")
    public ResponseEntity<List<Message>> getAllMessage(){
        List<Message> messages = messageService.getAllMsg();
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }
}
