package com.chatApplication.service;

import com.chatApplication.dto.*;
import com.chatApplication.exception.MessageContentNull;
import com.chatApplication.exception.UserNotFound;
import com.chatApplication.model.Message;
import com.chatApplication.model.User;
import com.chatApplication.repository.MessageRepository;
import com.chatApplication.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {


    @Autowired
    private final MessageRepository messageRepository;

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
            Utils.extractSenderAndMessage(hash,messages.get(i));
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

//        messages.forEach(message ->System.out.println(message.getSender().getUsername()+"    " + message.getReceiver().getUsername()));

        MessageFromSingleUserResponse response = new MessageFromSingleUserResponse();

        response.setData(new ArrayList<>());

//        ConversationResponse conversationResponse = new ConversationResponse();
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

    public Message sendMessage(String senderUsername, String receiverUsername, String content) throws MessageContentNull, UserNotFound {
        if(content == null){
            throw new MessageContentNull("Message can not be empty");
        }
        User sender = userService.findUserByUsername(senderUsername);
        User receiver = userService.findUserByUsername(receiverUsername);

        if(sender == null){
            throw new UserNotFound("sender Not found");
        }

        if(receiver == null){
            throw new UserNotFound("receiver Not found");
        }
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        return messageRepository.save(message);
    }

}
