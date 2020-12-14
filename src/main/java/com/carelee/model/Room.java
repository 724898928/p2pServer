package com.carelee.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Package: com.carelee.model
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:18
 * Copyright: Copyright (c) 2040
 */
public class Room {
    private String id;
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    public Room(String id ) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void addUser(User user) {
        if (null != user)
        userMap.put(user.getId(), user);
    }

    public Map<String, User> users() {
        return userMap;
    }

    public User getUser(String userId) {
        return userMap.get(userId);
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", userMap=" + userMap +
                '}';
    }
}
