package com.chatApplication.service;

import com.chatApplication.config.JwtUtil;
import com.chatApplication.dto.*;
import com.chatApplication.exception.ActionNotAllowed;
import com.chatApplication.exception.MessageContentNull;
import com.chatApplication.exception.UserNotFound;
import com.chatApplication.model.Message;
import com.chatApplication.model.User;
import com.chatApplication.repository.MessageRepository;
import com.chatApplication.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {


    @Autowired
    private final MessageRepository messageRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    private final UserService userService;

    public List<Message> getAllMsg() {
        return messageRepository.findAll();
    }

    public MessageFromSingleUserResponse getChatHistoryOfAUser(User receiver) {
        List<Message> messages =  messageRepository.findMessagesByReceiverOrderByTimestampDesc(receiver);


        HashMap<String, List<String>> hash = new HashMap<>();


        List<String> messagesFromReceiver = new ArrayList<>();
        for(int i = 0 ; i < messages.size() ; i++) {
            Message message = messages.get(i);
            Utils.extractSenderAndMessage(hash,message);
            message.setSeen(true);
            messageRepository.save(message);
        }

        MessageFromSingleUserResponse response = new MessageFromSingleUserResponse();
        response.setData(new ArrayList<>());
        Set<Map.Entry<String, List<String>>> entries = hash.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            MessageFromSingleSenderResponse messageFromSingleSenderResponse = new MessageFromSingleSenderResponse();

            messageFromSingleSenderResponse.setUsername(key);
            messageFromSingleSenderResponse.setMessages(value);


            List<MessageFromSingleSenderResponse> data = response.getData();
            data.add(messageFromSingleSenderResponse);
            response.setData(data);
        }
        if(response.getData() == null || response.getData().size() == 0 ){
            response.setStatus("No new messages");
            List<MessageFromSingleSenderResponse> data = new ArrayList<>();
            response.setData(data);
        }
        else {
            response.setStatus("You have message(s)");
        }
        return response;
    }

    public MessageFromSingleUserResponse getChatHistoryOfAUserWithFriend(User sender, User receiver) {
        List<Message> messages =  messageRepository.findMessagesBySenderAndReceiverOrderByTimestampDesc(sender,receiver);
        MessageFromSingleUserResponse response = new MessageFromSingleUserResponse();

        response.setData(new ArrayList<>());

        for(int i = 0 ; i < messages.size() ; i++) {
            Message  message = messages.get(i);
            String senderUserName = message.getSender().getUsername();
            String receiverUserName = message.getReceiver().getUsername();
            String text = message.getContent();
            System.out.println(senderUserName+ "  " + " " + receiverUserName + " " + text);
            MessageFromSingleSenderResponse messageFromSingleSenderResponse = new MessageFromSingleSenderResponse();

            messageFromSingleSenderResponse.setMessages(Arrays.asList(text));
            messageFromSingleSenderResponse.setUsername(senderUserName);

            List<MessageFromSingleSenderResponse> data = response.getData();
            data.add(messageFromSingleSenderResponse);

            response.setData(data);
        }
        return response;
    }

    public ResponseEntity<?> sendMessage(SendMessageDto msg, String token) throws MessageContentNull, UserNotFound, ActionNotAllowed {
        String content = msg.getText();
        String senderUsername = msg.getSender();
        String receiverUsername = msg.getReceiver();
        if(content == null){
            throw new MessageContentNull("Message can not be empty");
        }
        if(senderUsername == null){
            throw new UserNotFound("sender username Not found");
        }

        if(receiverUsername == null){
            throw new UserNotFound("receiver username Not found");
        }
        User sender = userService.findUserByUsername(senderUsername);
        User receiver = userService.findUserByUsername(receiverUsername);

        if(senderUsername == null){
            throw new UserNotFound("sender Not found");

        }

        if(receiverUsername == null){
            throw new UserNotFound("receiver username Not found");
        }


        var x = jwtUtil.extractUsername(token);
        boolean isValid = jwtUtil.isTokenValid(token,sender);
        if(isValid == false) {
             throw new ActionNotAllowed("you are not allowed");
        }
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        messageRepository.save(message);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

}
