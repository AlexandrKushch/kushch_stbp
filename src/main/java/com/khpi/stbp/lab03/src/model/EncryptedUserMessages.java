package com.khpi.stbp.lab03.src.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncryptedUserMessages {
    Map<String, List<List<BigInteger>>> userMessages = new HashMap<>();

    public Map<String, List<List<BigInteger>>> getAllMessages() {
        return userMessages;
    }

    public List<List<BigInteger>> getUserMessages(String id) {
        return userMessages.get(id);
    }

    public void addMessage(String id, BigInteger[] messages) {
        userMessages.get(id).add(List.of(messages));
    }

    public void addUser(String id) {
        userMessages.put(id, new ArrayList<>());
    }
}
