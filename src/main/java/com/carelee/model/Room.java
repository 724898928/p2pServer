package com.carelee.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Package: com.carelee.model
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:18
 * Copyright: Copyright (c) 2040
 */
public class Room {
    private static final Logger log = LogManager.getLogger(Room.class);
    private String id;
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    public Room(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addUser(User user) {
        if (null != user)
            this.userMap.put(user.getId(), user);
    }

    public Map<String, User> users() {
        return this.userMap;
    }

    public User getUser(String userId) {
        return this.userMap.get(userId);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void delUser(String userId) {
       User user = this.userMap.remove(userId);
       log.info("删除了用户 "+user.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", userMap=" + userMap +
                '}';
    }
}
