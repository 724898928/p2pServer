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
        return id.equals(user.id) &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        if (id == null)
            return 0;
        int result = 1;
        result =id.hashCode()+ 31 * result + (name == null ? 0 : name.hashCode());
        return result;
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
