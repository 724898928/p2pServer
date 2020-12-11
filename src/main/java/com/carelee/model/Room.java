package com.carelee.model;

/**
 * Package: com.carelee.model
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:18
 * Copyright: Copyright (c) 2040
 */
public class Room {
    private String id;
    private User user;
    private Session session;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", session=" + session +
                '}';
    }
}
