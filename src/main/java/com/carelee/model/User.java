package com.carelee.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.websocket.Session;
import java.util.Objects;

/**
 * Package: com.carelee.model
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:16
 * Copyright: Copyright (c) 2040
 */
public class User {
    private String id;
    private String name;
    @JSONField(serialize = false)
    private Session session;

    public User(Object id, Object name, Session session) {
        this.id = String.valueOf(id);
        this.name = String.valueOf(name);
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", session=" + session +
                '}';
    }
}
