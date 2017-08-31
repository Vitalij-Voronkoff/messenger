package com.vvoronkov.messenger.service;

import com.vvoronkov.messenger.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    public List<Message> getAllMessages(){
        Message m1 = new Message(1L, "Hello World", "Vitalii");
        Message m2 = new Message(2L, "Hello Jersey", "Vitalii");
        List<Message> messages = new ArrayList<>();
        messages.add(m1);
        messages.add(m2);
        return messages;
    }
}
