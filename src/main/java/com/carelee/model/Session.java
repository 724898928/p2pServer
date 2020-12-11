package com.carelee.model;

/**
 * Package: com.carelee.model
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:20
 * Copyright: Copyright (c) 2040
 */
public class Session {
    private String id;
    private User from;
    private User to;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
