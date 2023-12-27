package com.chatApplication.util;

import com.chatApplication.model.Message;

import java.util.*;

public class Utils {
    public static void extractSenderAndMessage(HashMap<String, List<String>> hashMap, Message message){
        String sender = message.getSender().getUsername().toLowerCase();
        String content = message.getContent() ;
        List<String> messagesFromReceiver = new ArrayList<>();
        if(hashMap.containsKey(sender)) {
             messagesFromReceiver = hashMap.get(sender);

        }
        else {
            messagesFromReceiver.add(content);
        }
        messagesFromReceiver.add(content);
        hashMap.put(sender, messagesFromReceiver);
    }
}
