package com.khpi.stbp.lab03.src.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMessages {
    Map<String, List<String>> userMessages = new HashMap<>();

    public Map<String, List<String>> getAllMessages() {
        return userMessages;
    }

    public void addMessage(String id, String message) {
        userMessages.get(id).add(message);
    }

    public void addUser(String id) {
        userMessages.put(id, new ArrayList<>());
    }

    public void clear() {
        for (String k :
                userMessages.keySet()) {
            userMessages.get(k).clear();
        }
    }
}
