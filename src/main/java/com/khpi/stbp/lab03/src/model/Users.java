package com.khpi.stbp.lab03.src.model;

import com.khpi.stbp.lab03.src.rsa.Key;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Users {
    Map<String, Key> users = new HashMap<>();

    public void addUser(String id, Key key) {
        users.put(id, key);
    }

    public Map<String, Key> getUsers() {
        return users;
    }
}
